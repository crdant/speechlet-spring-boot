package io.crdant.spring.speechlet.web.filter;

import io.crdant.spring.speechlet.web.SpeechletServletRequest;
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

