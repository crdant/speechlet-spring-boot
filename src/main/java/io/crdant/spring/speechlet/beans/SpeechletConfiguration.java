package io.crdant.spring.speechlet.beans;

import io.crdant.spring.speechlet.annotation.SlotArgumentResolver;
import io.crdant.spring.speechlet.handler.SpeechletValidationHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class SpeechletConfiguration extends WebMvcConfigurerAdapter  {

    @Autowired
    SpeechletValidationHandlerInterceptor speechletValidationHandlerInterceptor ;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SlotArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(speechletValidationHandlerInterceptor);
        super.addInterceptors(registry);
    }

}
