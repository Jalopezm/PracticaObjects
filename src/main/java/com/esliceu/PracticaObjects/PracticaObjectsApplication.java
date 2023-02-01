package com.esliceu.PracticaObjects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@SpringBootApplication
public class PracticaObjectsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticaObjectsApplication.class, args);
	}

//	@Override
//	public void addInterceptors(InterceptorRegistry registry){
//		registry.addInterceptor(new MyAuthInterceptor())
//				.addPathPatterns("/objects/**");
//	}
}
