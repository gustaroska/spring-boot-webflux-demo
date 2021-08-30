package com.demo;

import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;

@EnableWebFluxSecurity
public class SecurityConfig {

	
	@Value("${spring.security.oauth2.resourceserver.jwt.public-key-location}")
	RSAPublicKey key;

	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
		return http
//				.requestCache()
//			    .requestCache(NoOpServerRequestCache.getInstance()).and()
				.oauth2ResourceServer((resourceServer) -> resourceServer
						.jwt((jwt) -> jwt
								.jwtDecoder(jwtDecoder())
						)
				)
				.authorizeExchange()
				.pathMatchers("/api/**").authenticated()
				.pathMatchers("/stream/**").authenticated()
	            .pathMatchers("/**").permitAll()
	            .and()
//	            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
	            .httpBasic().disable()
				.build()
				
	            ;
	}
	

	@Bean
    public NimbusReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withPublicKey(this.key).build();
    }
    
}
