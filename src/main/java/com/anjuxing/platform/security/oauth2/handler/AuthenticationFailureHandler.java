package com.anjuxing.platform.security.oauth2.handler;

import com.anjuxing.platform.security.oauth2.util.OAuth2Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiongt
 * @Description
 */
@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper ;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        logger.debug("Exception message is :"+ exception.getMessage());
//        if (ResponseMode.JSON.equals()){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(OAuth2Constants.CONTENT_TYPE_APPLICATION_JSON);
            response.getWriter().write(objectMapper.writeValueAsString(new OAuth2Response<String>(exception.getMessage())));
//        } else {
//            super.onAuthenticationFailure(request, response, exception);
//        }

    }
}
