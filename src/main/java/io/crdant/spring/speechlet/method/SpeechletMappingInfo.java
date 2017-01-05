package io.crdant.spring.speechlet.method;

import io.crdant.spring.speechlet.handler.condition.IntentRequestCondition;
import io.crdant.spring.speechlet.handler.condition.LaunchRequestCondition;
import io.crdant.spring.speechlet.handler.condition.SessionEndedRequestCondition;
import io.crdant.spring.speechlet.handler.condition.SessionStartedRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;

public class SpeechletMappingInfo implements RequestCondition<SpeechletMappingInfo> {
    private final String name ;
    private final LaunchRequestCondition launchRequestCondition;
    private final SessionStartedRequestCondition sessionStartedRequestCondition;
    private final IntentRequestCondition intentRequestCondition;
    private final SessionEndedRequestCondition sessionEndedRequestCondition;

    public SpeechletMappingInfo(String name, LaunchRequestCondition launchRequestCondition,
                                SessionStartedRequestCondition sessionStartedRequestCondition, IntentRequestCondition intentRequestCondition,
                                SessionEndedRequestCondition sessionEndedRequestCondition)
    {
        this.name = name;
        this.launchRequestCondition = launchRequestCondition;
        this.sessionStartedRequestCondition = sessionStartedRequestCondition;
        this.intentRequestCondition = intentRequestCondition;
        this.sessionEndedRequestCondition = sessionEndedRequestCondition;
    }

    @Override
    public SpeechletMappingInfo combine(SpeechletMappingInfo other) {
        String name = this.name + "::" + other.getName() ;
        LaunchRequestCondition launch = this.launchRequestCondition.combine(other.getLaunchRequestCondition());
        SessionStartedRequestCondition started = this.sessionStartedRequestCondition.combine(other.getSessionStartedRequestCondition());
        IntentRequestCondition intent = this.intentRequestCondition.combine(other.getIntentRequestCondition());
        SessionEndedRequestCondition ended = this.sessionEndedRequestCondition.combine(other.getSessionEndedRequestCondition());

        return new SpeechletMappingInfo(this.name, launch, started, intent, ended);
    }

    @Override
    public SpeechletMappingInfo getMatchingCondition(HttpServletRequest request) {
        LaunchRequestCondition launch = this.launchRequestCondition.getMatchingCondition(request);
        SessionStartedRequestCondition started = this.sessionStartedRequestCondition.getMatchingCondition(request);
        IntentRequestCondition intent = this.intentRequestCondition.getMatchingCondition(request);
        SessionEndedRequestCondition ended = this.sessionEndedRequestCondition.getMatchingCondition(request);

        return new SpeechletMappingInfo(this.name, launch, started, intent, ended);
    }

    @Override
    public int compareTo(SpeechletMappingInfo other, HttpServletRequest request) {
        int result;

        result = this.launchRequestCondition.compareTo(other.getLaunchRequestCondition(), request);
        if (result != 0) {
            return result;
        }
        result = this.sessionStartedRequestCondition.compareTo(other.getSessionStartedRequestCondition(), request);
        if (result != 0) {
            return result;
        }
        result = this.intentRequestCondition.compareTo(other.getIntentRequestCondition(), request);
        if (result != 0) {
            return result;
        }
        result = this.sessionEndedRequestCondition.compareTo(other.getSessionEndedRequestCondition(), request);
        if (result != 0) {
            return result;
        }

        return 0;
    }

    public String getName() {
        return name;
    }

    public LaunchRequestCondition getLaunchRequestCondition() {
        return launchRequestCondition;
    }

    public SessionStartedRequestCondition getSessionStartedRequestCondition() {
        return sessionStartedRequestCondition;
    }

    public IntentRequestCondition getIntentRequestCondition() {
        return intentRequestCondition;
    }

    public SessionEndedRequestCondition getSessionEndedRequestCondition() {
        return sessionEndedRequestCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpeechletMappingInfo that = (SpeechletMappingInfo) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (launchRequestCondition != null ? !launchRequestCondition.equals(that.launchRequestCondition) : that.launchRequestCondition != null)
            return false;
        if (sessionStartedRequestCondition != null ? !sessionStartedRequestCondition.equals(that.sessionStartedRequestCondition) : that.sessionStartedRequestCondition != null)
            return false;
        if (intentRequestCondition != null ? !intentRequestCondition.equals(that.intentRequestCondition) : that.intentRequestCondition != null)
            return false;
        return sessionEndedRequestCondition != null ? sessionEndedRequestCondition.equals(that.sessionEndedRequestCondition) : that.sessionEndedRequestCondition == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (launchRequestCondition != null ? launchRequestCondition.hashCode() : 0);
        result = 31 * result + (sessionStartedRequestCondition != null ? sessionStartedRequestCondition.hashCode() : 0);
        result = 31 * result + (intentRequestCondition != null ? intentRequestCondition.hashCode() : 0);
        result = 31 * result + (sessionEndedRequestCondition != null ? sessionEndedRequestCondition.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpeechletMappingInfo{" +
                "name='" + name + '\'' +
                ", launchRequestCondition=" + launchRequestCondition +
                ", sessionStartedRequestCondition=" + sessionStartedRequestCondition +
                ", intentRequestCondition=" + intentRequestCondition +
                ", sessionEndedRequestCondition=" + sessionEndedRequestCondition +
                '}';
    }

}
