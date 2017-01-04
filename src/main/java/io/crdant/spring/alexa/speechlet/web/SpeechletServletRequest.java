package io.crdant.spring.alexa.speechlet.web;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SpeechletServletRequest extends HttpServletRequestWrapper {
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
