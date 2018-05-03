package com.fancience.session.support;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 拓展spring 登录的service
 * Created by Leonid on 18/3/26.
 */
public interface IFancienceUserDetailService extends UserDetailsService {

    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    /**
     * 通过用户名和appNo获取的用户信息
     * @param username
     * @param appNo
     * @return
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserByUsernameApp(String username, String appNo) throws UsernameNotFoundException;


}
