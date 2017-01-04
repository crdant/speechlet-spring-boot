package io.crdant.spring.alexa.speechlet.method;

import io.crdant.spring.alexa.speechlet.handler.condition.LaunchRequestCondition;
import io.crdant.spring.alexa.speechlet.handler.condition.SessionEndRequestCondition;
import io.crdant.spring.alexa.speechlet.handler.condition.SessionStartRequestCondition;
import io.crdant.spring.alexa.speechlet.handler.condition.SpeechletRequestCondition;
import io.crdant.spring.alexa.speechlet.handler.condition.IntentRequestCondition;

import org.springframework.web.servlet.mvc.condition.RequestCondition ;

import javax.servlet.http.HttpServletRequest;

public class SpeechletMappingInfo implements RequestCondition<SpeechletMappingInfo> {
    private final String name ;
    private final SpeechletRequestCondition speechletCondition ;
    private final LaunchRequestCondition launchCondition ;
    private final SessionStartRequestCondition startCondition ;
    private final IntentRequestCondition intentsCondition ;
    private final SessionEndRequestCondition endCondition ;

    public SpeechletMappingInfo(String name, SpeechletRequestCondition speechletCondition, LaunchRequestCondition launchCondition,
                                SessionStartRequestCondition startCondition, IntentRequestCondition intentsCondition,
                                SessionEndRequestCondition endCondition)
    {
        this.name = name;
        this.speechletCondition = speechletCondition;
        this.launchCondition = launchCondition;
        this.startCondition = startCondition;
        this.intentsCondition = intentsCondition;
        this.endCondition = endCondition;
    }

    @Override
    public SpeechletMappingInfo combine(SpeechletMappingInfo other) {
        return null;
    }

    @Override
    public SpeechletMappingInfo getMatchingCondition(HttpServletRequest request) {
        return null;
    }

    @Override
    public int compareTo(SpeechletMappingInfo other, HttpServletRequest request) {
        return 0;
    }

    public String getName() {
        return name;
    }

    public SpeechletRequestCondition getSpeechletCondition() {
        return speechletCondition;
    }

    public LaunchRequestCondition getLaunchCondition() {
        return launchCondition;
    }

    public SessionStartRequestCondition getStartCondition() {
        return startCondition;
    }

    public IntentRequestCondition getIntentsCondition() {
        return intentsCondition;
    }

    public SessionEndRequestCondition getEndCondition() {
        return endCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpeechletMappingInfo that = (SpeechletMappingInfo) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (speechletCondition != null ? !speechletCondition.equals(that.speechletCondition) : that.speechletCondition != null)
            return false;
        if (launchCondition != null ? !launchCondition.equals(that.launchCondition) : that.launchCondition != null)
            return false;
        if (startCondition != null ? !startCondition.equals(that.startCondition) : that.startCondition != null)
            return false;
        if (intentsCondition != null ? !intentsCondition.equals(that.intentsCondition) : that.intentsCondition != null)
            return false;
        return endCondition != null ? endCondition.equals(that.endCondition) : that.endCondition == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (speechletCondition != null ? speechletCondition.hashCode() : 0);
        result = 31 * result + (launchCondition != null ? launchCondition.hashCode() : 0);
        result = 31 * result + (startCondition != null ? startCondition.hashCode() : 0);
        result = 31 * result + (intentsCondition != null ? intentsCondition.hashCode() : 0);
        result = 31 * result + (endCondition != null ? endCondition.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpeechletMappingInfo{" +
                "name='" + name + '\'' +
                ", speechletCondition=" + speechletCondition +
                ", launchCondition=" + launchCondition +
                ", startCondition=" + startCondition +
                ", intentsCondition=" + intentsCondition +
                ", endCondition=" + endCondition +
                '}';
    }

}
