package io.crdant.spring.alexa.speechlet.web.filter;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier;
import io.crdant.spring.alexa.speechlet.web.SpeechletServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SpeechletRequestServletFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        HttpServletRequest speechletRequest = request;
        if (!(request instanceof SpeechletServletRequest)) {
            speechletRequest = new SpeechletServletRequest(request);
        }
        filterChain.doFilter(speechletRequest, response);
    }

}

