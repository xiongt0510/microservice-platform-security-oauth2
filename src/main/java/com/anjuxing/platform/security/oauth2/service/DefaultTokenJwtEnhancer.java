package com.anjuxing.platform.security.oauth2.service;

import org.apache.commons.collections.MapUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * @author xiongt
 * @Description  添加token的自定义默认类
 */
public class DefaultTokenJwtEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(MapUtils.EMPTY_MAP);
        return accessToken;
    }
}
