package br.com.api.projeto.model.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.api.projeto.model.exceptions.CustomAcessDeniedHandler;
import br.com.api.projeto.model.exceptions.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final TokenProvider tokenProvider;
	
	private static final String [] ENDPOINTS_LIBERADOS = {
			"/security/auth/signup",
			"/security/auth/login",
			"/security/test/free"
			
	};
	
	private static final String [] ENDPOINTS_ROOM_ROLEADMIN = {
			"/security/room/create"
	};
	
	private static final String [] ENDPOINTS_RESTRITOS_ADMIN = {
			"/security/test/needAdmin"
	};
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http
         .csrf()
         .disable()
         .authorizeHttpRequests(authorize -> authorize
                 .requestMatchers(ENDPOINTS_LIBERADOS).permitAll()
                 .requestMatchers(ENDPOINTS_RESTRITOS_ADMIN).hasAuthority("ROLE_ADMIN")
                 .requestMatchers(ENDPOINTS_ROOM_ROLEADMIN).hasAuthority("ROLE_ADMIN")
                 .anyRequest().authenticated())
         .sessionManagement(session -> session
                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
         .addFilterBefore(new JWTFilter(tokenProvider),UsernamePasswordAuthenticationFilter.class)
             .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint((AuthenticationEntryPoint) new CustomAuthenticationEntryPoint()))
             .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(new CustomAcessDeniedHandler()));

		 return http.build();
	}

		
}

