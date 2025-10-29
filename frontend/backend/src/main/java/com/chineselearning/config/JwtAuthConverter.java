package com.chineselearning.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Custom JWT converter để extract roles từ Keycloak token
 * Keycloak lưu roles trong realm_access.roles và resource_access.{client-id}.roles
 */
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = 
            new JwtGrantedAuthoritiesConverter();

    private final String resourceId = "chinese-learning-backend"; // Client ID trong Keycloak

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = jwt.getClaim("preferred_username");
        if (claimName == null) {
            claimName = jwt.getSubject();
        }
        return claimName;
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Set<GrantedAuthority> authorities = Stream.concat(
                extractRealmRoles(jwt).stream(),
                extractClientRoles(jwt).stream()
        ).collect(Collectors.toSet());

        return authorities;
    }

    /**
     * Extract roles từ realm_access.roles
     */
    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Collection<String> realmRoles;

        if (realmAccess == null || realmAccess.get("roles") == null) {
            return Set.of();
        }

        realmRoles = (Collection<String>) realmAccess.get("roles");
        
        return realmRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toSet());
    }

    /**
     * Extract roles từ resource_access.{client-id}.roles
     */
    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractClientRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource;
        Collection<String> resourceRoles;

        if (resourceAccess == null || resourceAccess.get(resourceId) == null) {
            return Set.of();
        }

        resource = (Map<String, Object>) resourceAccess.get(resourceId);
        resourceRoles = (Collection<String>) resource.get("roles");
        
        if (resourceRoles == null) {
            return Set.of();
        }

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toSet());
    }
}

