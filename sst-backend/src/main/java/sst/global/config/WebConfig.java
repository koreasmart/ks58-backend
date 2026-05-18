package sst.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final FileStorageConfig config;
	
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
