package io.crdant.tfinder.beans;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SpeechletRequestFilter extends OncePerRequestFilter {
    /**
     * Preserves the request body so that intents and slots can be handled by the appropriate handlers.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest cachedRequest = request;

        if (!(request instanceof ContentCachingRequestWrapper)) {
            cachedRequest = new ContentCachingRequestWrapper(request);
        }
        filterChain.doFilter(cachedRequest, response);
    }
}
