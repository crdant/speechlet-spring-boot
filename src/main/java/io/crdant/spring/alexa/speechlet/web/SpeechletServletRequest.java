package io.crdant.spring.alexa.speechlet.web;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class SpeechletServletRequest extends HttpServletRequestWrapper {
    private Context speechletContext ;
    private Session speechletSession ;
    private SpeechletRequest speechletRequest;
    private SpeechletRequestEnvelope<?> requestEnvelope ;

    public SpeechletServletRequest(HttpServletRequest request) {
        super(request);
        Context incomingSpeechletContext ;
        Session incomingSpeechletSession ;
        SpeechletRequest incomingSpeechletRequest ;

        try {
            byte[] buffer = IOUtils.toByteArray(request.getInputStream());
            this.requestEnvelope = SpeechletRequestEnvelope.fromJson(buffer);

            incomingSpeechletContext = requestEnvelope.getContext();
            incomingSpeechletSession = requestEnvelope.getSession();
            incomingSpeechletRequest = requestEnvelope.getRequest();

            this.speechletContext = speechletContext;
            this.speechletSession = speechletSession;
            this.speechletRequest = speechletRequest;
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create a speechlet from the provided HTTP request");
        }
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

    public <T> T getSpeechletRequest(Class<T> requiredType) {
        if (requiredType != null && requiredType.isInstance(speechletRequest)) {
            return (T) speechletRequest;
        }
        return null;
    }

}
