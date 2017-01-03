package io.crdant.spring.alexa.web.method;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

public class SpeechletMessageConverter extends AbstractHttpMessageConverter<Object> {

    public SpeechletMessageConverter() {
        super(MediaType.APPLICATION_JSON);
    }

    @Override
    public boolean supports(Class clazz) {
        return ( clazz.equals(SpeechletRequest.class) ) || ( clazz.equals(SpeechletResponse.class) ) ;
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage httpInputMessage)  throws IOException, HttpMessageNotReadableException {
        SpeechletRequestEnvelope<?> requestEnvelope = SpeechletRequestEnvelope.fromJson(httpInputMessage.getBody());
        return requestEnvelope.getRequest();
    }

    @Override
    protected void writeInternal(Object speechletObject, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
        responseEnvelope.setResponse((SpeechletResponse)speechletObject);
        responseEnvelope.toJson(httpOutputMessage.getBody());
    }

}
