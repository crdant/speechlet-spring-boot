package io.crdant.spring.alexa.speechlet.handler;

import com.amazon.speech.Sdk;
import com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier;
import com.amazon.speech.speechlet.verifier.TimestampSpeechletRequestVerifier;
import io.crdant.spring.alexa.speechlet.web.SpeechletServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class SpeechletValidationHandlerInterceptor extends HandlerInterceptorAdapter {

    @Value("${speechlet.validation.disable}")
    Boolean disable ;

    @Value("${speechlet.timestamp.tolerance}")
    Long tolerance ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ( !(request instanceof SpeechletServletRequest ) ) return false;
        SpeechletServletRequest speechletServletRequest = (SpeechletServletRequest) request ;
        return disable.booleanValue() || ( validSignature(speechletServletRequest) && validTimetamp(speechletServletRequest) );
    }

    private boolean validSignature(SpeechletServletRequest request) {
        try {
            SpeechletRequestSignatureVerifier.checkRequestSignature(request.getSerializedRequest(), request.getHeader(Sdk.SIGNATURE_REQUEST_HEADER),
                    request.getHeader(Sdk.SIGNATURE_CERTIFICATE_CHAIN_URL_REQUEST_HEADER));
            return true ;
        } catch ( SecurityException invalidSignature ) {
            return false ;
        }
    }

    private boolean validTimetamp(SpeechletServletRequest request) {
        TimestampSpeechletRequestVerifier verifier = new TimestampSpeechletRequestVerifier(tolerance.longValue(), TimeUnit.SECONDS);
        return verifier.verify(request.getSpeechletRequest(), request.getSpeechletSession());
    }
}
