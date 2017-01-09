package io.crdant.spring.speechlet.web.filter;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier;
import com.amazon.speech.speechlet.verifier.TimestampSpeechletRequestVerifier;
import io.crdant.spring.speechlet.annotation.Speechlet;
import io.crdant.spring.speechlet.web.SpeechletServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class SpeechletValidationServletFilter extends OncePerRequestFilter  {

    @Value("${speechlet.validation.disable}")
    Boolean disable ;

    @Value("${speechlet.timestamp.tolerance}")
    Long tolerance ;

    @Autowired
    ApplicationContext applicationContext ;

    private Set<String> applicationIds ;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {

        if ( ( request instanceof SpeechletServletRequest && ((SpeechletServletRequest)request).isForSpeechlet() )) {
            logger.debug("request " + request + " appears to be a speechlet: request.isForSpeechet() = " + ((SpeechletServletRequest) request).isForSpeechlet());

            SpeechletServletRequest speechletServletRequest = (SpeechletServletRequest) request;
            if (logger.isDebugEnabled()) {
                logger.debug("Validating speechlet execution for: " + speechletServletRequest.getApplicationId());
            }

            boolean allow = disable.booleanValue() ||
                    (validSignature(speechletServletRequest) && validTimetamp(speechletServletRequest) && validApplication(speechletServletRequest));
            if (allow) {
                filterChain.doFilter(speechletServletRequest, response);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Request violates security pre-conditions for an Alexa skill");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        this.applicationIds = new HashSet<String>();
        Map<String,Object> speechlets = applicationContext.getBeansWithAnnotation(Speechlet.class);
        for ( Map.Entry<String,Object> speechlet : speechlets.entrySet() ) {
            Class<?> bean = speechlet.getValue().getClass();
            Speechlet annotation = AnnotationUtils.findAnnotation(bean, Speechlet.class);
            applicationIds.add(annotation.value()[0]);
        }
        super.afterPropertiesSet();
    }

    private boolean validSignature(SpeechletServletRequest request) {
        logger.debug("testing signature");
        try {
            SpeechletRequestSignatureVerifier.checkRequestSignature(request.getSerializedRequest(), request.getHeader(Sdk.SIGNATURE_REQUEST_HEADER),
                    request.getHeader(Sdk.SIGNATURE_CERTIFICATE_CHAIN_URL_REQUEST_HEADER));
            return true ;
        } catch ( SecurityException invalidSignature ) {
            if ( logger.isDebugEnabled() ) {
                logger.debug("Invalid signature on request: " + invalidSignature.getMessage() );
            }
            return false ;
        }
    }

    private boolean validTimetamp(SpeechletServletRequest request) {
        logger.debug("testing timestamp");

        TimestampSpeechletRequestVerifier verifier = new TimestampSpeechletRequestVerifier(tolerance.longValue(), TimeUnit.SECONDS);
        return verifier.verify(request.getSpeechletRequest(), request.getSpeechletSession());
    }

    private boolean validApplication(SpeechletServletRequest request) {
        return applicationIds.contains(request.getApplicationId());
    }

}
