package com.h9.admin.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * Created by itservice on 2017/10/26.
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Resource
    private LoginAuthInterceptor loginAuthInterceptor;
    @Resource
    private RequestLogInterceptor requestLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginAuthInterceptor).addPathPatterns("/**");
        registry.addInterceptor(requestLogInterceptor).addPathPatterns("/**");
    }
}
