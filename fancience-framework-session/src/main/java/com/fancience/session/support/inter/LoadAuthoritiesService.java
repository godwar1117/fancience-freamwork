package com.fancience.session.support.inter;

import com.fancience.session.user.SecurityUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 加载系统权限信息
 * Created by Leonid on 18/3/19.
 */
public interface LoadAuthoritiesService {

    Collection<GrantedAuthority> loadAuthorities(SecurityUser securityUser);
}
