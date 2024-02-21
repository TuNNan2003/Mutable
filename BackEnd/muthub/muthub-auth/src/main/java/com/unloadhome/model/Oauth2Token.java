package com.unloadhome.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Oauth2Token {
    private String token;
    private String refreshToken;
    private String tokenHead;
    private int expires;
}
