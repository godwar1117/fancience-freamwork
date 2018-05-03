package com.fancience.dubbo.config;

import com.fancience.dubbo.processor.DubboBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Dubbo 初始化配置文件
 * Created by Leonid on 18/3/14.
 */
@ImportResource("classpath:dubbo.xml")
@Configuration
public class DubboConfig {

    @Bean
    public DubboBeanPostProcessor dubboBeanPostProcessor() {
        return new DubboBeanPostProcessor();
    }

}
