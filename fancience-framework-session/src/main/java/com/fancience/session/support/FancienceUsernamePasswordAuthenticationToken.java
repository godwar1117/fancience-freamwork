package com.fancience.session.support;

import com.fancience.session.support.constants.SystemApplicationConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leonid on 18/3/16.
 */
public class FancienceUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = -5956583650401505559L;

    //private boolean loadedAuthorities = false;
    // application 编号
    private String appNo;
    // 多平台多权限
    private Map<String, Collection<GrantedAuthority>> appAuthorities = new HashMap<>();

    public FancienceUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public FancienceUsernamePasswordAuthenticationToken(Object principal, Object credentials, String appNo) {
        super(principal, credentials);
        this.appNo = appNo;
    }

    public FancienceUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
    public FancienceUsernamePasswordAuthenticationToken(Object principal, Object credentials, String appNo, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.appNo = appNo;
    }

    public void putAuthorities(String appNo, Collection<GrantedAuthority> authorities) {
        appAuthorities.put(appNo, authorities);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        //return super.getAuthorities();
        // 获取appCode
        String appNo = System.getProperty(SystemApplicationConstants.APP_NO);
        return appNo == null?null:(Collection)this.appAuthorities.get(appNo);
    }

    public boolean isLoadedAuthorities(String appNo) {
        return appAuthorities.get(appNo) != null;
    }

    /*
    public void loadOverAuthorities() {
        this.loadedAuthorities = true;
    }

    public boolean isLoadedAuthorities() {
        return this.loadedAuthorities;
    }
    */

    public String getAppNo() {
        return appNo;
    }
}
