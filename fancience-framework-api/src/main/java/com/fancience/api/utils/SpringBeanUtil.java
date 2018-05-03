package com.fancience.api.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * spring Bean 服务
 * Created by Leonid on 18/3/23.
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        if(context.containsBean(beanName)){
            return (T) context.getBean(beanName);
        }else{
            return null;
        }
    }

    public static <T> T getBeanOfType(Class<T> clazz) {
        if(context.getBean(clazz) != null) {
            return context.getBean(clazz);
        }
        return null;
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> baseType){
        return context.getBeansOfType(baseType);
    }
}
