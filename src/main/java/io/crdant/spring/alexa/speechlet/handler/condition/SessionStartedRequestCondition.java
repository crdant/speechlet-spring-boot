package io.crdant.spring.alexa.speechlet.handler.condition;

import com.amazon.speech.speechlet.*;

import java.util.Collection;
import java.util.Set;

public class SessionStartedRequestCondition extends SpeechletLifecycleRequestCondition<SessionStartedRequestCondition> {

    public SessionStartedRequestCondition(String... applicationIds) {
        super(applicationIds);
    }

    public SessionStartedRequestCondition(Collection<String> applicationIds ) {
        super(applicationIds);
    }

    @Override
    protected SessionStartedRequestCondition getMatchingConditionInternal(Context speechletContext, Session speechletSession, SpeechletRequest speechletRequest) {
        if ( !(speechletRequest instanceof SessionStartedRequest ) ) return null ;
        return super.getMatchingConditionInternal(speechletContext, speechletSession, speechletRequest);
    }

    @Override
    protected SessionStartedRequestCondition newInstance(Set<String> applicationIds) {
        return new SessionStartedRequestCondition(applicationIds);
    }

}
