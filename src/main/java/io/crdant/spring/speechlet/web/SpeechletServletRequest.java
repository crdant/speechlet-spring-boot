package io.crdant.spring.speechlet.web;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class SpeechletServletRequest extends HttpServletRequestWrapper {
    public static final Logger logger = LoggerFactory.getLogger(SpeechletServletRequest.class);

    private Context speechletContext ;
    private Session speechletSession ;
    private SpeechletRequest speechletRequest;
    private byte[] serializedRequest ;
    SpeechletRequestEnvelope<?> requestEnvelope;

    public SpeechletServletRequest(HttpServletRequest request) {
        super(request);
        Context incomingSpeechletContext ;
        Session incomingSpeechletSession ;
        SpeechletRequest incomingSpeechletRequest ;

        try {
            this.serializedRequest = IOUtils.toByteArray(request.getInputStream());
            this.requestEnvelope = SpeechletRequestEnvelope.fromJson(serializedRequest);
            incomingSpeechletContext = requestEnvelope.getContext();
            incomingSpeechletSession = requestEnvelope.getSession();
            incomingSpeechletRequest = requestEnvelope.getRequest();

            this.speechletContext = incomingSpeechletContext;
            this.speechletSession = incomingSpeechletSession;
            this.speechletRequest = incomingSpeechletRequest;
            logger.info("Processed speechlet request and made its components available as part of the request");
        } catch (Exception e) {
            logger.error("Not a speechlet request. Request content cached and available for reading.");
            throw new IllegalArgumentException("Not a speechlet request");
        }
    }

    public boolean isForSpeechlet () {
        return getSpeechletContext() != null && getSpeechletSession() != null && getSpeechletRequest() != null ;
    }

    public boolean isLaunchRequest() {
        return ( speechletRequest instanceof LaunchRequest );
    }

    public boolean isSessionStartedRequest() {
        return ( speechletRequest instanceof SessionStartedRequest );
    }

    public boolean isIntentRequest() {
        return ( speechletRequest instanceof IntentRequest );
    }

    public boolean isSessionEndedRequest() {
        return ( speechletRequest instanceof SessionEndedRequest );
    }

    public String getApplicationId() {
        return getSpeechletSession().getApplication().getApplicationId();
    }

    public Intent getIntent() {
        if ( !isIntentRequest() ) return null ;
        return ((IntentRequest)speechletRequest).getIntent() ;
    }

    public String getIntentName() {
        if ( !(speechletRequest instanceof IntentRequest ) ) return null ;
        return ((IntentRequest)speechletRequest).getIntent().getName() ;
    }

    public Context getSpeechletContext() {
        return speechletContext;
    }

    public Session getSpeechletSession() {
        return speechletSession;
    }

    public SpeechletRequest getSpeechletRequest() {
        return speechletRequest;
    }

    public byte[] getSerializedRequest() {
        return serializedRequest;
    }

    public <T> T getSpeechletRequest(Class<T> requiredType) {
        if (requiredType != null && requiredType.isInstance(speechletRequest)) {
            return (T) speechletRequest;
        }
        return null;
    }

    public SpeechletRequestEnvelope<?> getRequestEnvelope() {
        return this.requestEnvelope ;
    }
}
