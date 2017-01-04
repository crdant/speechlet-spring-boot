package io.crdant.spring.alexa.speechlet.handler.condition;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public abstract class AbstractSpeechletRequestCondition<T extends AbstractSpeechletRequestCondition<T>> extends AbstractRequestCondition<T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public T getMatchingCondition(HttpServletRequest request) {
        try {
            byte[] buffer = IOUtils.toByteArray(request.getInputStream());
            SpeechletRequestEnvelope<?> requestEnvelope = SpeechletRequestEnvelope.fromJson(buffer);
            Context speechletContext = requestEnvelope.getContext();
            Session speechletSession = requestEnvelope.getSession();
            SpeechletRequest speechletRequest = requestEnvelope.getRequest();
            if ( logger.isDebugEnabled() ) {
                logger.debug("Mapping a request for a speechlet request of type " + speechletRequest.getClass().getName());
            }
            return getMatchingConditionInternal(speechletContext, speechletSession, speechletRequest);
        } catch ( Exception e ) {
            logger.error("Caught an exception that needs to be handled better: " + e.getMessage());
        }
        return null;
    }

    abstract protected T getMatchingConditionInternal(Context speechletContext, Session speechletSession, SpeechletRequest speechletRequest ) ;

    @Override
    protected String getToStringInfix() {
        return " || ";
    }
}
