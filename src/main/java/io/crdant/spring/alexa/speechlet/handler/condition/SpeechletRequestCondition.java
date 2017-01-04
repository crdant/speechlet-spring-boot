package io.crdant.spring.alexa.speechlet.handler.condition;

import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class SpeechletRequestCondition extends AbstractSpeechletRequestCondition<SpeechletRequestCondition> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Set<String> applicationIds;

    public SpeechletRequestCondition(String... applicationIds) {
        this.applicationIds = new LinkedHashSet<String>() ;
        this.applicationIds.addAll(Arrays.asList(applicationIds));
    }

    public SpeechletRequestCondition(Collection<String> applicationIds) {
        this.applicationIds = new LinkedHashSet<String>() ;
        this.applicationIds.addAll(applicationIds) ;
    }

    @Override
    protected SpeechletRequestCondition getMatchingConditionInternal(Context speechletContext, Session speechletSession, SpeechletRequest speechletRequest) {
        return null;
    }

    @Override
    public SpeechletRequestCondition combine(SpeechletRequestCondition other) {
        return null;
    }

    @Override
    public SpeechletRequestCondition getMatchingCondition(HttpServletRequest request) {
        return null;
    }

    @Override
    public int compareTo(SpeechletRequestCondition other, HttpServletRequest request) {
        return 0;
    }

    @Override
    protected Collection<?> getContent() {
        return null;
    }
}
