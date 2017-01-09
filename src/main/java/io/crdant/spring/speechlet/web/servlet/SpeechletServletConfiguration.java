package io.crdant.spring.speechlet.web.servlet;

import io.crdant.spring.speechlet.web.filter.SpeechletRequestServletFilter;
import io.crdant.spring.speechlet.web.filter.SpeechletValidationServletFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Servlet;

@Configuration
public class SpeechletServletConfiguration extends SpringBootServletInitializer {

    @Autowired
    BootifulSpeechletServlet speechletServlet ;

    @Autowired
    SpeechletRequestServletFilter speechletRequestFilter;

    @Autowired
    SpeechletValidationServletFilter speechletValidationFilter;

    @Bean(name="speechletServletRegistration")
    @ConditionalOnBean(value=io.crdant.spring.speechlet.web.servlet.BootifulSpeechletServlet.class)
    public ServletRegistrationBean speechletServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean();
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setServlet(speechletServlet);
        // registration.setEnabled(false);
        return registration;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpeechletServletConfiguration.class);
    }

    @Bean
    public FilterRegistrationBean speechletRequestSerlvetFilter () {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(speechletRequestFilter);
        registration.setOrder(Ordered.LOWEST_PRECEDENCE + 1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean speechletValidationFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(speechletValidationFilter);
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }

}
