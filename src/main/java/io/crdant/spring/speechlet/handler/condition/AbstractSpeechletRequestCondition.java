package io.crdant.spring.speechlet.handler.condition;

import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import io.crdant.spring.speechlet.web.SpeechletServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractSpeechletRequestCondition<T extends AbstractSpeechletRequestCondition<T>> extends AbstractRequestCondition<T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public T getMatchingCondition(HttpServletRequest request) {
        SpeechletServletRequest speechletServletRequest = (SpeechletServletRequest) WebUtils.getNativeRequest(request, SpeechletServletRequest.class);
        if ( speechletServletRequest == null ) return null ;
        Context speechletContext = speechletServletRequest.getSpeechletContext();
        Session speechletSession = speechletServletRequest.getSpeechletSession();
        SpeechletRequest speechletRequest = speechletServletRequest.getSpeechletRequest();
        return getMatchingConditionInternal(speechletContext, speechletSession, speechletRequest);
    }

    abstract protected T getMatchingConditionInternal(Context speechletContext, Session speechletSession, SpeechletRequest speechletRequest ) ;

    @Override
    protected String getToStringInfix() {
        return " || ";
    }

}
