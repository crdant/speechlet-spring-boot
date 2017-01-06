package io.crdant.spring.speechlet.handler.condition;

import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SpeechletRequest;

import java.util.Collection;
import java.util.Set;

public class SessionEndedRequestCondition extends SpeechletLifecycleRequestCondition<SessionEndedRequestCondition> {

    public SessionEndedRequestCondition () {
        super();
    }

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

    @Override
    public String toString() {
        return "SessionEndedRequestCondition{" +
                "applicationIds=" + this.getApplicationIds() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SessionEndedRequestCondition that = (SessionEndedRequestCondition) o;

        return this.getApplicationIds() != null ? this.getApplicationIds().equals(that.getApplicationIds()) : this.getApplicationIds() == null;
    }
}
