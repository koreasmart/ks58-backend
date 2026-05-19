package sst.global.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import sst.global.utils.MdcUtils;

@Slf4j
@Component
@Order(1)
public class LoggingFilter implements Filter{
	
	public static final String ATTR_START_TIME  = "startTime";
    public static final String ATTR_CACHED_BODY = "cachedBody";
    
	private static final int MAX_PAYLOAD_SIZE = 100 * 1024; // 100KB

	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        ContentCachingRequestWrapper wrappedReq =
                new ContentCachingRequestWrapper(httpRequest, MAX_PAYLOAD_SIZE);

        wrappedReq.setAttribute(ATTR_START_TIME, System.currentTimeMillis());

        MdcUtils.put(MdcUtils.TRACE_ID,  MdcUtils.generateTraceId());
        MdcUtils.put(MdcUtils.CLIENT_IP, getClientIp(httpRequest));

        try {
            chain.doFilter(wrappedReq, response);
        } finally {
            // chain 완료 후 body 캐시에 채워짐 — 속성에 저장
            wrappedReq.setAttribute(ATTR_CACHED_BODY, wrappedReq.getContentAsByteArray());
            MdcUtils.clear();
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.isBlank()) ip = request.getRemoteAddr();
        return ip != null ? ip.split(",")[0].trim() : "-";
    }
}
