package com.anjuxing.platform.security.oauth2.handler;

import org.springframework.security.core.AuthenticationException;

/**
 * @author xiongt
 * @Description
 */
public class UserAuthenticationException extends AuthenticationException {

    public UserAuthenticationException(String msg) {
        super(msg);
    }
}
