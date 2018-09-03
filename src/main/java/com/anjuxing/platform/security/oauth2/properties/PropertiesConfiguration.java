package com.anjuxing.platform.security.oauth2.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiongt
 * @Description  开启配置
 */
@Configuration
@EnableConfigurationProperties(OAuth2Properties.class)
public class PropertiesConfiguration {
}
