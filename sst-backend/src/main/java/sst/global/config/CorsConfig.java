package sst.global.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

	@Value("${cors.allowed-origins:http://localhost:5173}")
	private List<String> allowOrigins;

	private final List<String> allowMethods = List.of(
			"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
	);

	@Bean
	@Primary
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		// 허용 출처
		config.setAllowedOrigins(allowOrigins);

		// 허용 HTTP Method
		config.setAllowedMethods(allowMethods);

		// 허용 Header
		config.setAllowedHeaders(List.of("Content-Type", "Accept", "X-Requested-With"));
		
		// 브라우저 쿠키/인증정보 포함 허용
		config.setAllowCredentials(true);

		// preflight 요청 캐시 시간
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return source;
	}
}