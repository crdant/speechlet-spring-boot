package io.crdant.spring.speechlet.web.servlet;

import io.crdant.spring.speechlet.web.filter.SpeechletValidationServletFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;

import javax.servlet.Servlet;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("io.crdant.spring.speechlet")
public class SpeechletServletApplication extends SpringBootServletInitializer {

    @Autowired
    Servlet speechletServlet ;

    @Autowired
    SpeechletValidationServletFilter speechletValidationFilter;

    @SuppressWarnings("serial")
    @Bean
    public Servlet dispatcherServlet() {
        return speechletServlet;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpeechletServletApplication.class);
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
}
