package org.mrshim.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.mrshim.apigateway.utils.JwtAuthConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

   private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity security) {
        security
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange(authorizeExchangeSpec ->

                        authorizeExchangeSpec.anyExchange().permitAll()
/*                        authorizeExchangeSpec.pathMatchers("/eureka/**")
                                .permitAll()
                                .pathMatchers("/register")
                                .permitAll()
                                .pathMatchers("/login")
                                .permitAll()
                                .anyExchange().authenticated()*/


                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                        oAuth2ResourceServerSpec.jwt(configure->configure.jwtAuthenticationConverter(jwtAuthConverter)));


        return security.build();

    }


}
