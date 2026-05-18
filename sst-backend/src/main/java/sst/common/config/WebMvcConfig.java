package sst.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {
	@Value("${component.file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	/* 기본 파일 업로드 경로 */
        registry.addResourceHandler("/attachment/**")
                .addResourceLocations("file:///" + uploadDir + "/");
    }
}
