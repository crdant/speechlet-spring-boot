package io.crdant.spring.speechlet.web.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.servlet.Servlet;

@SpringBootConfiguration
@EnableAutoConfiguration
public class SpeechletServletApplication extends SpringBootServletInitializer {

    @Autowired
    Servlet speechletServlet ;

    @SuppressWarnings("serial")
    @Bean
    public Servlet dispatcherServlet() {
        return speechletServlet;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpeechletServletApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpeechletServletApplication.class, args);
    }
}
