package com.fancience.web.web.auth;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Leonid on 18/3/20.
 */
@RestController
@RequestMapping("/security")
public class SecurityController {
    private static final String EXCEPTION_403_KEY = "SPRING_SECURITY_403_EXCEPTION";
    private static final String EXCEPTION_401_KEY = "SPRING_SECURITY_401_EXCEPTION";

    @RequestMapping("/noLogin")
    public Object noLogin(HttpServletRequest request, HttpServletResponse response) {
        //AuthenticationException exception = (AuthenticationException) request.getAttribute(EXCEPTION_401_KEY);
        return "nologin";
    }

    @RequestMapping("/noAuth")
    public Object noAuth(HttpServletRequest request, HttpServletResponse response) {
        //AccessDeniedException exception = (AccessDeniedException) request.getAttribute(EXCEPTION_403_KEY);
        return "noAuth";
    }


}
