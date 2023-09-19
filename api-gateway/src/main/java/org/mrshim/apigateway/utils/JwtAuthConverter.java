package org.mrshim.apigateway.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class JwtAuthConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt source) {

        Collection<GrantedAuthority> roles=extractAuthorities(source);


        return Mono.just(new JwtAuthenticationToken(source,roles));
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt)
    {

        if (jwt.getClaim("realm_access")!=null)
        {
            Map<String, Object> realmAccess=jwt.getClaim("realm_access");
            ObjectMapper objectMapper=new ObjectMapper();

            TypeReference<ArrayList<String>> typeReference = new TypeReference<ArrayList<String>>() {};

            ArrayList<String> keyCloakRoles = objectMapper.convertValue(realmAccess.get("roles"), typeReference);

            List<GrantedAuthority> roles=new ArrayList<>();

            for (String keyCloakRole : keyCloakRoles) {
                roles.add(new SimpleGrantedAuthority("ROLE_"+keyCloakRole));
            }

            return roles;


        }

        return new ArrayList<>();



    }


}
