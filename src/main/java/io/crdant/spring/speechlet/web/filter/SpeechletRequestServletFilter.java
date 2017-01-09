package io.crdant.spring.speechlet.web.filter;

import io.crdant.spring.speechlet.web.SpeechletServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

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
        HttpServletRequest requestWrapper = new ContentCachingRequestWrapper(request);
        if (!(request instanceof SpeechletServletRequest)) {
            try {
                requestWrapper = new SpeechletServletRequest(requestWrapper);
            } catch ( IllegalArgumentException notSpeechlet ) {
                if ( logger.isTraceEnabled() ) logger.trace("Request is not for a speechlet, forwarding a cached copy of the original request");
            }
        }
        filterChain.doFilter(requestWrapper, response);
    }

}

