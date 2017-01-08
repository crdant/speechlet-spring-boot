package io.crdant.spring.speechlet.web.filter;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier;
import com.amazon.speech.speechlet.verifier.TimestampSpeechletRequestVerifier;
import io.crdant.spring.speechlet.web.SpeechletServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class SpeechletValidationServletFilter extends OncePerRequestFilter {

    @Value("${speechlet.validation.disable}")
    Boolean disable ;

    @Value("${speechlet.timestamp.tolerance}")
    Long tolerance ;

    @Autowired
    ApplicationContext applicationContext ;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        if ( logger.isDebugEnabled() ) {
            logger.debug("Validating speechlet execution");
        }

        SpeechletServletRequest speechletServletRequest = (SpeechletServletRequest) request ;
        boolean allow = disable.booleanValue() ||
                ( validSignature(speechletServletRequest) && validTimetamp(speechletServletRequest) && validApplication(speechletServletRequest)) ;

        if ( allow ) {
            filterChain.doFilter(speechletServletRequest, response);
        } else {
            response.sendError( HttpServletResponse.SC_FORBIDDEN, "Request violates security pre-conditions for an Alexa skill" ) ;
        }
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
        logger.debug("will test the application ids here someday...");
        return true ;
    }

}
