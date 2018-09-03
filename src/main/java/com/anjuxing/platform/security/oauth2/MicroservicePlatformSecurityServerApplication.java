package com.anjuxing.platform.security.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author xiongt
 * @Description
 */

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class MicroservicePlatformSecurityServerApplication {

    public static void main(String [] args){
        SpringApplication.run(MicroservicePlatformSecurityServerApplication.class,args);
    }
}
