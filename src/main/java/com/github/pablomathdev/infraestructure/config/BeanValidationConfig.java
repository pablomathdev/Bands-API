package com.github.pablomathdev.infraestructure.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


@Configuration
public class BeanValidationConfig {


    @Bean
    LocalValidatorFactoryBean validator(MessageSource messageSource) {
		
    	LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
    	factoryBean.setValidationMessageSource(messageSource);
    	
    	return factoryBean;
	}
}
