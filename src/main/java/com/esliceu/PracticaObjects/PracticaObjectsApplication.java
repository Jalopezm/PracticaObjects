package com.esliceu.PracticaObjects;

import com.esliceu.PracticaObjects.interceptors.MyAuthInterceptor;
import org.aopalliance.intercept.Interceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class PracticaObjectsApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(PracticaObjectsApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyAuthInterceptor()).addPathPatterns("/objects/**","/settings");
    }
}
