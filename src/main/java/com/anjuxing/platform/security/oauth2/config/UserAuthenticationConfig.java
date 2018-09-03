package com.anjuxing.platform.security.oauth2.config;

import com.anjuxing.platform.security.oauth2.handler.UserAuthenticationFilter;
import com.anjuxing.platform.security.oauth2.handler.UserAuthenticationProvider;
import com.anjuxing.platform.security.oauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author xiongt
 * @Description
 */
@Component
public class UserAuthenticationConfig
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity>
{

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private UserService userService;



    @Override
    public void configure(HttpSecurity http) throws Exception {

        UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter();
        userAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        userAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);
        userAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        UserAuthenticationProvider userAuthenticationProvider = new UserAuthenticationProvider();
        userAuthenticationProvider.setUserService(userService);

        http.authenticationProvider(userAuthenticationProvider)
                .addFilterAfter(userAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
    }
}
