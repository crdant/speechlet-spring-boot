package io.crdant.spring.speechlet.web.servlet;

import io.crdant.spring.speechlet.web.filter.SpeechletRequestServletFilter;
import io.crdant.spring.speechlet.web.filter.SpeechletValidationServletFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import java.io.IOException;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("io.crdant")
public class SpeechletServletApplication extends SpringBootServletInitializer {

    @Autowired
    BootifulSpeechletServlet speechletServlet ;

    @Autowired
    SpeechletRequestServletFilter speechletRequestFilter;

    @Autowired
    SpeechletValidationServletFilter speechletValidationFilter;

    @Bean
    public Servlet dispatcherServlet() {
        return new BootifulSpeechletServlet();
        // return speechletServlet;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpeechletServletApplication.class);
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

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpeechletServletApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate () {
        return new RestTemplate() ;
    }
}
