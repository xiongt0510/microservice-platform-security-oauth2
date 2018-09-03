package com.anjuxing.platform.security.oauth2.service;

import com.anjuxing.platform.security.oauth2.model.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author xiongt
 * @Description  用户接口这个接口调用远程服务
 */
@FeignClient("microserver-platform-security-authority")
public interface UserService {

    /**
     * 根据用户名进行登录
     * @param username
     * @return
     */
    @PostMapping("/user/username")
    User getUserByUsername(@RequestParam("username") String username);

    /**
     * 根据手机登录
     * @param mobile
     * @return
     */
    @PostMapping("user/mobile")
    User getUserByMobile(@RequestParam("mobile") String mobile);


    /**
     * 获得所有用户
     * @return
     */
    @GetMapping("/user/list")
    List<User> userList();

}
