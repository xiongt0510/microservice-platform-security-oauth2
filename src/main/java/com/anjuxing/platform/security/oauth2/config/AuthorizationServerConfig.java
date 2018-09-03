package com.anjuxing.platform.security.oauth2.config;

import com.anjuxing.platform.security.oauth2.properties.OAuth2ClientProperties;
import com.anjuxing.platform.security.oauth2.properties.OAuth2Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xiongt 认证服务器配置
 * @Description
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    @Autowired
    private OAuth2Properties authorizationProperties;

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

    /**
     * 认证端点配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);

        //这段配置主要是转换为 jwt token ,并为jwt token 添加字段
        if (Objects.nonNull(jwtAccessTokenConverter)){

            if (Objects.nonNull(jwtTokenEnhancer)){
                TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
                List<TokenEnhancer> enhancers = new ArrayList<>();
                enhancers.add(jwtTokenEnhancer);
                enhancers.add(jwtAccessTokenConverter);
                enhancerChain.setTokenEnhancers(enhancers);
                endpoints.tokenEnhancer(enhancerChain);
            }
            endpoints.accessTokenConverter(jwtAccessTokenConverter);
        }

    }

    /**
     * 访问客户端配置,客户端配置是放在配置文件或从读取数据库
     *
     * 读取多个客户端
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();

        if (authorizationProperties.getClients() != null){
            for (OAuth2ClientProperties client : authorizationProperties.getClients()){
                builder.withClient(client.getClientId())
                        .secret(client.getClientSecret())
                        .accessTokenValiditySeconds(client.getAccessTokenValidateSeconds())
                        .authorizedGrantTypes(client.getAuthorizedGrantTypes())
                        .scopes(client.getScopes());
            }
        }
    }


}
