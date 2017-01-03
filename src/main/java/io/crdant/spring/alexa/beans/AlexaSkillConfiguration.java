package io.crdant.tfinder.beans;

import io.crdant.tfinder.bind.annotation.SlotArgumentResolver;
import io.crdant.tfinder.web.method.IntentRequestMappingHandlerMapping;
import io.crdant.tfinder.web.method.SpeechletMessageConverter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class AlexaSkillConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public FilterRegistrationBean speechletFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        SpeechletRequestFilter speechletFilter = new SpeechletRequestFilter();
        registrationBean.setFilter(speechletFilter);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public IntentRequestMappingHandlerMapping intentRequestHandlerMapping () {
        return new IntentRequestMappingHandlerMapping();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SlotArgumentResolver());
    }

    @Bean
    public HttpMessageConverter speechletMessageConverter() {
        return new SpeechletMessageConverter();
    }
}
