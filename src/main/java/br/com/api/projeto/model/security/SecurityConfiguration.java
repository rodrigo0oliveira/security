package br.com.api.projeto.model.security;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.api.projeto.model.exceptions.CustomAcessDeniedHandler;
import br.com.api.projeto.model.exceptions.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@SecurityScheme(name = SecurityConfiguration.SECURITY,type = SecuritySchemeType.HTTP,bearerFormat = "JWT",scheme = "bearer")
public class SecurityConfiguration{

	public static final String SECURITY = "bearerAuth";
	
	private final TokenProvider tokenProvider;

	
	private static final String [] ENDPOINTS_LIBERADOS = {
			"/security/auth/signup",
			"/security/auth/login",
			"/security/auth/changepassword/{username}"
	};
	
	private static final String [] ENDPOINTS_RESERVE_ROLE_GUEST= {
			"/security/reserve/create",
			"/security/reserve/findAll/me"
	};
	
	private static final String [] ENDPOINTS_RESTRITOS_ADMIN = {
			//room
			"/security/room/create",
			"/security/room/edit/{roomnumber}",
			"/security/room/findAll",
			//reserve
			"/security/reserve/findAll",
			"/security/reserve/delete/{id}",
			
	};
	
	private static final String [] ENDPOINTS_PRECISA_LOGIN = {
			"/security/room/findAll/auth"
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

		 http.csrf()
         .disable()
         .authorizeHttpRequests(authorize -> authorize
				 .requestMatchers(PathRequest.toH2Console()).permitAll()
                 .requestMatchers(ENDPOINTS_LIBERADOS).permitAll()
                 .requestMatchers(ENDPOINTS_RESERVE_ROLE_GUEST).hasAuthority("ROLE_GUEST")
                 .requestMatchers(ENDPOINTS_RESTRITOS_ADMIN).hasAuthority("ROLE_ADMIN")
                 .requestMatchers(ENDPOINTS_PRECISA_LOGIN).authenticated()
				 .requestMatchers("/h2-console/**").permitAll()
				 .requestMatchers("/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                 .anyRequest().authenticated())
         .sessionManagement(session -> session
                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				 .addFilterBefore(new JWTFilter(tokenProvider),UsernamePasswordAuthenticationFilter.class)
             .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint((AuthenticationEntryPoint) new CustomAuthenticationEntryPoint()))
             .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(new CustomAcessDeniedHandler()));

		 return http.build();
	}

}

