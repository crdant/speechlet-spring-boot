package io.crdant.spring.alexa.speechlet.handler.condition;

import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SpeechletRequest;

import java.util.Collection;
import java.util.Set;

public class SessionEndedRequestCondition extends SpeechletLifecycleRequestCondition<SessionEndedRequestCondition> {

    public SessionEndedRequestCondition(String... applicationIds) {
        super(applicationIds);
    }

    public SessionEndedRequestCondition(Collection<String> applicationIds ) {
        super(applicationIds);
    }

    @Override
    protected SessionEndedRequestCondition getMatchingConditionInternal(Context speechletContext, Session speechletSession, SpeechletRequest speechletRequest) {
        if ( !(speechletRequest instanceof SessionEndedRequest) ) return null ;
        return super.getMatchingConditionInternal(speechletContext, speechletSession, speechletRequest);
    }

    @Override
    protected SessionEndedRequestCondition newInstance(Set<String> applicationIds) {
        return new SessionEndedRequestCondition(applicationIds);
    }

}
