package com.fancience.web.web.support;

import com.fancience.web.web.auth.AuthURLConstants;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Leonid on 18/3/20.
 */
public class FancienceNoLoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String SPRING_SECURITY_401_EXCEPTION = "SPRING_SECURITY_401_EXCEPTION";

    private String forwardUrl = AuthURLConstants.SECURITY_NO_LOGIN;

    public FancienceNoLoginAuthenticationEntryPoint(){}

    public FancienceNoLoginAuthenticationEntryPoint(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        request.setAttribute(SPRING_SECURITY_401_EXCEPTION, authException);
        response.setStatus(401);
        RequestDispatcher dispatcher = request.getRequestDispatcher(forwardUrl);
        dispatcher.forward(request, response);
    }
}
