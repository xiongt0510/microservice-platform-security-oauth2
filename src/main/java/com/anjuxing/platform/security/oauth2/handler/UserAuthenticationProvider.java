package com.anjuxing.platform.security.oauth2.handler;

import com.anjuxing.platform.security.oauth2.model.EntryType;
import com.anjuxing.platform.security.oauth2.model.User;
import com.anjuxing.platform.security.oauth2.model.UserParam;
import com.anjuxing.platform.security.oauth2.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * @author xiongt
 * @Description
 */
public class UserAuthenticationProvider implements AuthenticationProvider {

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UserAuthenticationToken userAuthenticationToken  = (UserAuthenticationToken)authentication;

        UserParam userParam = (UserParam) userAuthenticationToken.getPrincipal();

        User user = null;
        if (EntryType.USERNAME.equals(userParam.getEntryType())) {
            user = userService.getUserByUsername(userParam.getUsername()) ;
        } else if (EntryType.MOBILE.equals(userParam.getEntryType())) {
            user = userService.getUserByMobile(userParam.getMobile());
        }

        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        //检查密码是否对
        additionalAuthenticationChecks(user ,userParam);

        UserDetails userDetails = new UserDetailsImpl(user);

        //把从数据库得到信息的再次封装到 token中
        UserAuthenticationToken authenticationResult = new UserAuthenticationToken(userDetails,userDetails.getAuthorities());

        //把请求信息设置 进去
        authenticationResult.setDetails(userAuthenticationToken.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UserAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private void additionalAuthenticationChecks(User user,
                                                  UserParam userParam)
            throws AuthenticationException {


        if (!EntryType.USERNAME.equals(userParam.getEntryType())){
            return;
        }

        if (StringUtils.isEmpty(userParam.getPassword()) ) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

//        String encodePassword = passwordEncoder.encode(user.getPassword());
        if (!user.getPassword().equals(userParam.getPassword())) {
            logger.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
