package com.anjuxing.platform.security.oauth2.controller;

import com.anjuxing.platform.security.oauth2.properties.OAuth2Properties;
import com.anjuxing.platform.security.oauth2.model.EntryType;
import com.anjuxing.platform.security.oauth2.model.User;
import com.anjuxing.platform.security.oauth2.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xiongt
 * @Description
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private OAuth2Properties oAuth2Properties;




}
