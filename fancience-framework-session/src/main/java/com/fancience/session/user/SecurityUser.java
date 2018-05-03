package com.fancience.session.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 自定义的安全用户
 * Created by Leonid on 18/3/16.
 */
public class SecurityUser extends User {

    private static final long serialVersionUID = 3345689878551084904L;
    // 用户主键id
    private Long userId;
    // 用户凡智统一id
    private String fancienceId;

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public SecurityUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFancienceId() {
        return fancienceId;
    }

    public void setFancienceId(String fancienceId) {
        this.fancienceId = fancienceId;
    }
}
