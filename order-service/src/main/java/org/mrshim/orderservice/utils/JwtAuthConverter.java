package org.mrshim.orderservice.utils;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {

        Collection<GrantedAuthority> roles = extractAuthorities(source);


        return new JwtAuthenticationToken(source, roles);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {

        if (jwt.getClaim("realm_access") != null) {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            ObjectMapper objectMapper = new ObjectMapper();

            TypeReference<ArrayList<String>> typeReference = new TypeReference<>() {
            };

            ArrayList<String> keyCloakRoles = objectMapper.convertValue(realmAccess.get("roles"), typeReference);

            List<GrantedAuthority> roles = new ArrayList<>();

            for (String keyCloakRole : keyCloakRoles) {
                roles.add(new SimpleGrantedAuthority("ROLE_" + keyCloakRole.toUpperCase()));
            }

            return roles;


        }

        return new ArrayList<>();


    }
}
