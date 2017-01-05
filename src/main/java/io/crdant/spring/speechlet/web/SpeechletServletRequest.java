package io.crdant.spring.speechlet.web;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
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

    public SpeechletServletRequest(HttpServletRequest request) {
        super(request);
        Context incomingSpeechletContext ;
        Session incomingSpeechletSession ;
        SpeechletRequest incomingSpeechletRequest ;

        try {
            this.serializedRequest = IOUtils.toByteArray(request.getInputStream());
            SpeechletRequestEnvelope<?> requestEnvelope = SpeechletRequestEnvelope.fromJson(serializedRequest);

            incomingSpeechletContext = requestEnvelope.getContext();
            incomingSpeechletSession = requestEnvelope.getSession();
            incomingSpeechletRequest = requestEnvelope.getRequest();

            this.speechletContext = speechletContext;
            this.speechletSession = speechletSession;
            this.speechletRequest = speechletRequest;
            logger.info("Processed speechlet request and made its components available as part of the request.");

        } catch (Exception e) {
            logger.info("Not a speechlet request. Request content cached and available for reading.");
        }
    }

    public boolean isForSpeechlet () {
        return getSpeechletContext() != null && getSpeechletSession() != null && getSpeechletRequest() != null ;
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

}
