package com.anjuxing.platform.security.oauth2.config;

import com.anjuxing.platform.security.oauth2.util.OAuth2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author xiongt
 * @Description 资源配置
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig
        extends ResourceServerConfigurerAdapter
{


    @Autowired
    private UserAuthenticationConfig userAuthenticationConfig;



    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
           .apply(userAuthenticationConfig)
           .and()
          .authorizeRequests()
          .antMatchers(
                  OAuth2Constants.URL_USER_USERNAME,
                  OAuth2Constants.URL_USER_MOBILE

          ).permitAll()
          .anyRequest()
          .authenticated();
    }
}
