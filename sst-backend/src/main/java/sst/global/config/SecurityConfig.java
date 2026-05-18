	package sst.global.config;
	
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
	import org.springframework.security.config.annotation.web.builders.HttpSecurity;
	import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
	import org.springframework.security.config.http.SessionCreationPolicy;
	import org.springframework.security.web.SecurityFilterChain;
	import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
	import org.springframework.web.cors.CorsConfigurationSource;
	
	import jakarta.servlet.DispatcherType;
	import jakarta.servlet.http.HttpServletResponse;
	import lombok.RequiredArgsConstructor;
	import sst.global.security.filter.JwtAuthenticationFilter;
	import sst.global.security.handler.OAuth2SuccessHandler;
	import sst.global.security.service.CustomOAuth2UserService;
	
	@Configuration
	@EnableWebSecurity
	@EnableMethodSecurity
	@RequiredArgsConstructor
	public class SecurityConfig {
	
	    private final CorsConfigurationSource corsConfigurationSource;
	    private final JwtAuthenticationFilter jwtAuthenticationFilter;
	    private final CustomOAuth2UserService customOAuth2UserService;
	    private final OAuth2SuccessHandler oAuth2SuccessHandler;
	
	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf(config -> config.disable())
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .cors(cors -> cors.configurationSource(corsConfigurationSource))
	            .authorizeHttpRequests(auth -> auth
	                // 에러 페이지 포워딩 시 인증 블락(403)을 방지
	                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
	                // 업록드 경로에 대한 설정
	                .requestMatchers("/uploads/**").permitAll() 
	                // 로그아웃/내정보는 인증 필요
	                .requestMatchers("/api/auth/logout", "/api/auth/me").authenticated()
	                // 회원가입/로그인 허용
	                .requestMatchers("/api/auth/**").permitAll()
	                // 고객지원 조회 허용
	                .requestMatchers(
	            		"/api/customersupport/notice",
				        "/api/customersupport/faq",
				        "/api/comments/**"
	                ).permitAll()
	                // 볼거리 API 허용
	                .requestMatchers("/api/see/**").permitAll()
	                // 홈/메인페이지 카드 조회 API 허용
	                .requestMatchers("/api/home/**").permitAll()
	                // 업로드 이미지 접근 허용
	                .requestMatchers("/uploads/**").permitAll()
	                // 먹거리 API 허용
	                .requestMatchers("/api/food/**").permitAll()
	                // 잘거리 API 허용
	                .requestMatchers("/api/sleep/**").permitAll()
	                // 놀거리 API 허용
	                .requestMatchers("/api/play/**").permitAll()
	                // 관리자 API는 관리자만 허용
	                .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
	                // fastAPI 허용
	                .requestMatchers("/api/ai/travel/**").permitAll()
	                // AI 일정 허용
	                .requestMatchers("/api/ai/schedule/detail").permitAll()
	                .requestMatchers("/api/ai/schedule/my").permitAll()
	                .requestMatchers("/api/ai/schedule/save").authenticated()
	                .requestMatchers("/api/ai/schedule/update").authenticated()
	                .requestMatchers("/api/ai/schedule/delete").authenticated()
	                .requestMatchers("/api/ai/schedule/copy").authenticated()
	                // 리뷰 API 허용
	                .requestMatchers("/api/reviews/**").permitAll()
	                .requestMatchers("/api/place/**").permitAll()
	                .requestMatchers("/api/wishlist/**").permitAll()
	                .requestMatchers("/api/search/**").permitAll()
	                .requestMatchers("/api/community/**").permitAll()
	                // 그 외 모든 요청은 인증 필요
	                .anyRequest().authenticated()
	            )
	            .oauth2Login(oauth2 -> oauth2
	                .userInfoEndpoint(userInfo -> userInfo
	                    .userService(customOAuth2UserService)
	                )
	                .successHandler(oAuth2SuccessHandler)
	            )
	            .exceptionHandling(exception ->
	                exception
	                    .authenticationEntryPoint((request, response, authException) ->
	                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
	                    .accessDeniedHandler((request, response, accessDeniedException) ->
	                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
	            )
	            .addFilterBefore(jwtAuthenticationFilter,
	                UsernamePasswordAuthenticationFilter.class);
	
	        return http.build();
	    }
	}