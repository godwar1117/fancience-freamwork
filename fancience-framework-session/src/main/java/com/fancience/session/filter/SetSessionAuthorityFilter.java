package com.fancience.session.filter;

import com.fancience.session.support.FancienceUsernamePasswordAuthenticationToken;
import com.fancience.session.support.constants.SystemApplicationConstants;
import com.fancience.session.support.inter.LoadAuthoritiesService;
import com.fancience.session.user.SecurityUser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * 初次访问加载当前用户权限的filter
 * Created by Leonid on 18/3/16.
 */
public class SetSessionAuthorityFilter extends GenericFilterBean implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        // 加载权限用户权限
        LoadAuthoritiesService loadAuthoritiesService = applicationContext.getBean(LoadAuthoritiesService.class);
        if (SecurityContextHolder.getContext().getAuthentication() instanceof FancienceUsernamePasswordAuthenticationToken) {
            String appNo = System.getProperty(SystemApplicationConstants.APP_NO);
            // 如果没有加载权限
            if (!(((FancienceUsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())).isLoadedAuthorities(appNo)) {
                /*
                Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
                SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
                Collection<? extends GrantedAuthority> grantedAuthorities = loadAuthoritiesService.loadAuthorities(principal);
                FancienceUsernamePasswordAuthenticationToken token = new FancienceUsernamePasswordAuthenticationToken(
                        principal, credentials,
                        loadAuthoritiesService == null ? (Collection)null
                                : grantedAuthorities
                );
                token.setDetails(details);
                */
                SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Collection<GrantedAuthority> grantedAuthorities = loadAuthoritiesService.loadAuthorities(principal);
                if (!grantedAuthorities.isEmpty()) {
                    FancienceUsernamePasswordAuthenticationToken token = (FancienceUsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                    token.putAuthorities(appNo, grantedAuthorities);
                    //token.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(token);
                    request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
                }
                // 设置成已加载
                //(((FancienceUsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())).loadOverAuthorities();
            }
        }
        // 继续执行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
