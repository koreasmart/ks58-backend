package sst.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import lombok.RequiredArgsConstructor;
import sst.global.interceptor.LogInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final FileStorageConfig config;
	private final LogInterceptor logInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns(
				"/error",        // 에러 페이지
				"/favicon.ico"   // 브라우저 아이콘 요청
		);
	}

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
            .addResourceHandler("/attachment/**")
            .addResourceLocations(config.getOSResourceUri())
            .setCachePeriod(3600)
			.resourceChain(true)
			.addResolver(new PathResourceResolver());
    }
    

}
