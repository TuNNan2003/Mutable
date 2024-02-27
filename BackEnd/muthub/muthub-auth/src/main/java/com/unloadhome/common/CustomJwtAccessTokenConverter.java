package com.unloadhome.common;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {
    private final UserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();

    public CustomJwtAccessTokenConverter() {
        super();
    }

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, ?> response = super.convertAccessToken(token, authentication);

        if(authentication.getOAuth2Request().getRequestParameters().containsKey("switch_auth")){
            ((Map<String, Object>) response.get("authorities")).put("additionalAuthority", "ROLE_user");
        }
        return response;
    }
}
