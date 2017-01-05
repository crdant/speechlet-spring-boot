package io.crdant.spring.alexa.beans;

import io.crdant.spring.alexa.annotation.SlotArgumentResolver;
import io.crdant.spring.alexa.speechlet.handler.SpeechletHandlerMapping;
import io.crdant.spring.alexa.speechlet.handler.SpeechletValidationHandlerInterceptor;
import io.crdant.spring.alexa.speechlet.method.SpeechletMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
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

    @Bean("io.crdant.spring.alexa.speechlet.handler.SpeechletHandlerMapping")
    public HandlerMapping speechletHandlerMapping () {
        return new SpeechletHandlerMapping();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(speechletValidationHandlerInterceptor);
        super.addInterceptors(registry);
    }

    @Bean("io.crdant.spring.alexa.speechlet.method.SpeechletHandlerMapping")
    public HttpMessageConverter speechletMessageConverter() {
        return new SpeechletMessageConverter();
    }

}
