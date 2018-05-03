package com.fancience.session.config;

import com.fancience.session.support.FancienceHeaderHttpSessionStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * 初始化文件
 * Created by Leonid on 18/3/14.
 */
@Configuration
@EnableRedisHttpSession(redisNamespace = "fancience", maxInactiveIntervalInSeconds = 60*30)
public class SessionConfig {

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return FancienceHeaderHttpSessionStrategy.xAuthToken();
    }
}
