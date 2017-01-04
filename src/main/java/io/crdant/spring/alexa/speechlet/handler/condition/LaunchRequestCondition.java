package io.crdant.spring.alexa.speechlet.handler.condition;

import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;

import java.util.Collection;
import java.util.Set;

public class LaunchRequestCondition extends SpeechletLifecycleRequestCondition<LaunchRequestCondition> {

    public LaunchRequestCondition(String... applicationIds) {
        super(applicationIds);
    }

    public LaunchRequestCondition(Collection<String> applicationIds ) {
        super(applicationIds);
    }

    @Override
    protected LaunchRequestCondition getMatchingConditionInternal(Context speechletContext, Session speechletSession, SpeechletRequest speechletRequest) {
        if ( !(speechletRequest instanceof LaunchRequest ) ) return null ;
        return super.getMatchingConditionInternal(speechletContext, speechletSession, speechletRequest);
    }

    @Override
    protected LaunchRequestCondition newInstance(Set<String> applicationIds) {
        return new LaunchRequestCondition(applicationIds);
    }

}
