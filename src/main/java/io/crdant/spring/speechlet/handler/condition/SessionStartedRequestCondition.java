package io.crdant.spring.speechlet.handler.condition;

import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletRequest;

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

    @Override
    public String toString() {
        return "SessionStartedRequestCondition{" +
                "applicationIds=" + this.getApplicationIds() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SessionStartedRequestCondition that = (SessionStartedRequestCondition) o;

        return this.getApplicationIds() != null ? this.getApplicationIds().equals(that.getApplicationIds()) : this.getApplicationIds() == null;
    }

}
