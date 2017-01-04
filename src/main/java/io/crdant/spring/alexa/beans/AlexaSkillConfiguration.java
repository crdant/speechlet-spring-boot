package io.crdant.spring.alexa.beans;

import io.crdant.spring.alexa.annotation.SlotArgumentResolver;
import io.crdant.spring.alexa.speechlet.method.SpeechletMessageConverter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class AlexaSkillConfiguration extends WebMvcConfigurerAdapter  {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SlotArgumentResolver());
    }

    @Bean
    public HttpMessageConverter speechletMessageConverter() {
        return new SpeechletMessageConverter();
    }

}
