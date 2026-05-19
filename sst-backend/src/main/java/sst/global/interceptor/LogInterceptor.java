package sst.global.interceptor;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import sst.global.filter.LoggingFilter;
import sst.global.security.domain.CustomUserDetails;
import sst.global.utils.MdcUtils;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final int MAX_BODY_LEN = 1000;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String contentType = request.getContentType();
        String queryParam  = extractQueryParam(request);

        StringBuilder sb = new StringBuilder();
        sb.append("\n=======================Access Log Start===========================");
        sb.append("\nPORT        ::::    ").append(request.getLocalPort());
        sb.append("\nSERVERNAME  ::::    ").append(request.getServerName());
        sb.append("\nHTTP METHOD ::::    ").append(request.getMethod());
        sb.append("\nURI         ::::    ").append(request.getRequestURI());
        sb.append("\nCLIENT IP   ::::    ").append(MdcUtils.get(MdcUtils.CLIENT_IP));
        //sb.append("\nTRACE ID    ::::    ").append(MdcUtils.get(MdcUtils.TRACE_ID));
        sb.append("\nUSER ID     ::::    ").append(extractUserId());

        if (!queryParam.isEmpty()) {
            sb.append("\nPARAMETER   ::::    ").append(queryParam);
        }

        if (contentType != null) {

            // JSON
            if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                sb.append("\nBODY        ::::    ").append(extractJsonBody(request));
            }

            // 폼 데이터
            else if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                sb.append("\nBODY        ::::    ").append(extractFormBody(request));
            }

            // multipart — 요청 데이터 + 파일 개수
            else if (contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                sb.append("\nBODY        ::::    ").append(extractFormBody(request));
                sb.append("\nFILE COUNT  ::::    ").append(extractFileCount(request));
            }
        }

        sb.append("\n=======================Access Log End=============================");

        log.info(sb.toString());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        if (!(handler instanceof HandlerMethod)) return;

        if (ex != null) {
        	StringBuilder sb = new StringBuilder();
            sb.append("\nEXCEPTION   ::::    ")
              .append(ex.getClass().getSimpleName())
              .append(" : ")
              .append(ex.getMessage());
            log.error(sb.toString(), ex);
        }
    }

    // QueryString 파라미터 추출
    private String extractQueryParam(HttpServletRequest request) {
        return Collections.list(request.getParameterNames())
                .stream()
                .map(name -> name + "=" + request.getParameter(name))
                .collect(Collectors.joining(", "));
    }

    // application/json body 추출
    private String extractJsonBody(HttpServletRequest request) {
        Object cached = request.getAttribute(LoggingFilter.ATTR_CACHED_BODY);
        if (!(cached instanceof byte[] bytes) || bytes.length == 0) return "(empty)";
        String body = new String(bytes, StandardCharsets.UTF_8).trim();
        return body.length() > MAX_BODY_LEN
                ? body.substring(0, MAX_BODY_LEN) + "...(truncated)"
                : body;
    }

    // application/x-www-form-urlencoded, multipart 텍스트 필드 추출
    private String extractFormBody(HttpServletRequest request) {
        String result = Collections.list(request.getParameterNames())
                .stream()
                .map(name -> name + "=" + request.getParameter(name))
                .collect(Collectors.joining(", "));
        return result.isEmpty() ? "(empty)" : result;
    }

    // multipart 파일 개수 추출
    private int extractFileCount(HttpServletRequest request) {
        try {
            if (request instanceof MultipartHttpServletRequest multipartRequest) {
                return multipartRequest.getFileMap().size();
            }
        } catch (Exception e) {
            log.warn("File count 추출 실패 : {}", e.getMessage());
        }
        return 0;
    }

    // SecurityContextHolder — Interceptor에서 가장 간단한 방법
    private String extractUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "anonymous";
        }

        Object principal = authentication.getPrincipal();

        // Spring Security UserDetails 사용 시
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        // @AuthenticationPrincipal 커스텀 객체 사용 시
        // ex) CustomUserDetails, LoginUser 등 본인 클래스로 변경
        if (principal instanceof CustomUserDetails customUser) {
            return customUser.getUsername(); // 본인 메서드명으로 변경
        }

        return principal.toString();
    }
}