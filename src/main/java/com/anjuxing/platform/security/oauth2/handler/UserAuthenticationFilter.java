package com.anjuxing.platform.security.oauth2.handler;

import com.anjuxing.platform.security.oauth2.model.EntryType;
import com.anjuxing.platform.security.oauth2.model.UserParam;
import com.anjuxing.platform.security.oauth2.util.OAuth2Constants;
import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiongt
 * @Description
 */
public class UserAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /** 请求的mobile字段 */
    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";

    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    public static final String SPRING_SECURITY_FORM_ENTRY_TYPE_KEY = "entryType";

    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;

    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;

    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

    private String entryTypeParameter = SPRING_SECURITY_FORM_ENTRY_TYPE_KEY;


    //是否仅仅支持post 请求
    private boolean postOnly = true;


    /**
     * 当前仅仅是 mobile认证请求，post 方法时，才使用此过滤器
     */
    public UserAuthenticationFilter() {

        super(new OrRequestMatcher(
                new AntPathRequestMatcher(OAuth2Constants.URL_USER_USERNAME, HttpMethod.POST.toString())
                ,new AntPathRequestMatcher(OAuth2Constants.URL_USER_MOBILE, HttpMethod.POST.toString())
        ));



//        super(new AntPathRequestMatcher(OAuth2Constants.URL_USER_USERNAME, HttpMethod.POST.toString()));
    }



    /**
     * 获取mobile 参数的值
     * @param request
     * @return
     */
    protected String obtainMobile(HttpServletRequest request) {
        return trim(request.getParameter(mobileParameter));
    }

    private String trim(String parameter){
        String temp = parameter;

        temp = StringUtils.isEmpty(temp) ? "": temp.trim();

        return temp;
    }

    /**
     * 获取用户名 参数的值
     * @param request
     * @return
     */
    protected String obtainUsername(HttpServletRequest request) {
        return trim(request.getParameter(usernameParameter));
    }

    /**
     * 获取密码 参数的值
     * @param request
     * @return
     */
    protected String obtainPassword(HttpServletRequest request) {

        return trim(request.getParameter(passwordParameter));
    }


    /**
     * 获取登录类型
     * @param request
     * @return
     */
    protected String obtainEntryType(HttpServletRequest request) {
        return trim(request.getParameter(entryTypeParameter));
    }

    /**
     * 把request 请求信息设置 到 MobileAuthenticationToken中进行封装
     * @param request
     * @param authRequest
     */
    protected void setDetails(HttpServletRequest request,
                              UserAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //如果不是一个post的请求
        if (postOnly && !request.getMethod().equals("POST")){
            throw  new UserAuthenticationException("Authentication method not supported:"+request.getMethod());
        }

        UserParam userParam = new UserParam();
        userParam.setEntryType(EntryType.valueOf(obtainEntryType(request)));
        userParam.setMobile(obtainMobile(request));
        userParam.setPassword(obtainPassword(request));
        userParam.setUsername(obtainUsername(request));



        UserAuthenticationToken userAuthenticationToken = new UserAuthenticationToken(userParam);

        // Allow subclasses to set the "details" property
        setDetails(request,userAuthenticationToken);

        return this.getAuthenticationManager().authenticate(userAuthenticationToken);
    }


}
