package com.unloadhome.controller;

import com.unloadhome.common.Result;
import com.unloadhome.model.Oauth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private TokenEndpoint tokenEndpoint;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public Result postAccessToken(Principal principal, @RequestParam Map<String, String> param) {
        System.out.println(param);
        try {
            OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, param).getBody();
            Oauth2Token oauth2Token = Oauth2Token.builder()
                    .token(oAuth2AccessToken.getValue())
                    .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                    .expires(oAuth2AccessToken.getExpiresIn())
                    .tokenHead("Bearer ")
                    .build();
            return Result.succ(oauth2Token);
        }
        catch (Exception e){
            System.out.println(e);
            LOGGER.error(e.getMessage());
            return Result.fail(e.getMessage());
        }

    }

    @RequestMapping(value = "/switch", method = RequestMethod.POST)
    public Result SwitchAuth(Principal principal, @RequestParam Map<String, String> param) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_REPOOWNER"));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        try {
            OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, param).getBody();
            Oauth2Token oauth2Token = Oauth2Token.builder()
                    .token(oAuth2AccessToken.getValue())
                    .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                    .expires(oAuth2AccessToken.getExpiresIn())
                    .tokenHead("Bearer ")
                    .build();
            return Result.succ(oauth2Token);
        }
        catch (Exception e){
            System.out.println(e);
            LOGGER.error(e.getMessage());
            return Result.fail(e.getMessage());
        }
    }
}
