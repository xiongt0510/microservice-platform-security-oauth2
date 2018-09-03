package com.anjuxing.platform.security.oauth2.config;

import com.anjuxing.platform.security.oauth2.properties.OAuth2Properties;
import com.anjuxing.platform.security.oauth2.service.DefaultTokenJwtEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author xiongt
 * @Description token 存储配置
 */
@Configuration
public class TokenStoreConfig {

    /**
     * redis 存储token 配置
     * 当前有且有
     * security.auth2.tokenStore=redis 时生效
     */
    @Configuration
    @ConditionalOnProperty(prefix = "security.auth2" ,name = "tokenStore" ,havingValue = "redis")
    public static class RedisTokenStoreConfig {

        @Autowired
        private RedisConnectionFactory redisConnectionFactory;

        @Bean
        public TokenStore redisTokenStore() {
            return  new RedisTokenStore(redisConnectionFactory);
        }

    }

    /**
     * 配置jwt 令牌
     * 配置 security.auth2.tokenStore=jwt 或 不配置或错时生效
     */
    @Configuration
    @ConditionalOnProperty(prefix = "security.auth2" , name = "tokenStore" ,havingValue = "jwt", matchIfMissing = true)
    public static class JwtConfig {

        @Autowired
        private OAuth2Properties authorizationProperties;

        @Bean
        @Autowired
        public TokenStore jwtTokenStore(JwtAccessTokenConverter jwtAccessTokenConverter){
            return  new JwtTokenStore(jwtAccessTokenConverter);
        }

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter(){
            JwtAccessTokenConverter jwtAccessTokenConverter  = new JwtAccessTokenConverter();
            //设置签名的key
            jwtAccessTokenConverter.setSigningKey(authorizationProperties.getJwtSigningKey());
            return jwtAccessTokenConverter;
        }

        /**
         * 当spring 容器找不到 TokenEnhancer实现时会添加此bean 到容 器中
         * 可以添加其他实现来覆盖
         * @return
         */
        @Bean
        @ConditionalOnMissingBean(TokenEnhancer.class)
        public TokenEnhancer jwtTokenEnhancer(){
            return  new DefaultTokenJwtEnhancer();
        }

    }
}
