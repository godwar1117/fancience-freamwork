package com.fancience.session.configurers;

import com.fancience.session.filter.FancienceUsernamePasswordAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 凡智登录配置
 * Created by Leonid on 18/3/16.
 */
public class FancienceLoginConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractAuthenticationFilterConfigurer<H, FancienceLoginConfigurer<H>, FancienceUsernamePasswordAuthenticationFilter> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public FancienceLoginConfigurer() {
        super(new FancienceUsernamePasswordAuthenticationFilter(), (String)null);
        this.usernameParameter("username");
        this.passwordParameter("password");
    }

    public FancienceLoginConfigurer<H> loginPage(String loginPage) {
        return (FancienceLoginConfigurer)super.loginPage(loginPage);
    }

    public FancienceLoginConfigurer<H> usernameParameter(String usernameParameter) {
        ((FancienceUsernamePasswordAuthenticationFilter)this.getAuthenticationFilter()).setUsernameParameter(usernameParameter);
        return this;
    }

    public FancienceLoginConfigurer<H> passwordParameter(String passwordParameter) {
        ((FancienceUsernamePasswordAuthenticationFilter)this.getAuthenticationFilter()).setPasswordParameter(passwordParameter);
        return this;
    }

    public FancienceLoginConfigurer<H> failureForwardUrl(String forwardUrl) {
        this.failureHandler(new ForwardAuthenticationFailureHandler(forwardUrl));
        return this;
    }

    public FancienceLoginConfigurer<H> successForwardUrl(String forwardUrl) {
        this.successHandler(new ForwardAuthenticationSuccessHandler(forwardUrl));
        return this;
    }

    public void init(H http) throws Exception {
        super.init(http);
        this.initDefaultLoginFilter(http);
    }

    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    private String getUsernameParameter() {
        return ((FancienceUsernamePasswordAuthenticationFilter)this.getAuthenticationFilter()).getUsernameParameter();
    }

    private String getPasswordParameter() {
        return ((FancienceUsernamePasswordAuthenticationFilter)this.getAuthenticationFilter()).getPasswordParameter();
    }

    @Override
    public void configure(H http) throws Exception {
        try {
            super.configure(http);
        } catch (Exception e) {
            logger.warn("spring 安全框架 filter 顺序加载处理=========> 异常:{}", e.getMessage());
        } finally {
            http.addFilterBefore(this.getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        }
    }

    private void initDefaultLoginFilter(H http) {
        DefaultLoginPageGeneratingFilter loginPageGeneratingFilter = (DefaultLoginPageGeneratingFilter)http.getSharedObject(DefaultLoginPageGeneratingFilter.class);
        if(loginPageGeneratingFilter != null && !this.isCustomLoginPage()) {
            loginPageGeneratingFilter.setFormLoginEnabled(true);
            loginPageGeneratingFilter.setUsernameParameter(this.getUsernameParameter());
            loginPageGeneratingFilter.setPasswordParameter(this.getPasswordParameter());
            loginPageGeneratingFilter.setLoginPageUrl(this.getLoginPage());
            loginPageGeneratingFilter.setFailureUrl(this.getFailureUrl());
            loginPageGeneratingFilter.setAuthenticationUrl(this.getLoginProcessingUrl());
        }

    }
}
