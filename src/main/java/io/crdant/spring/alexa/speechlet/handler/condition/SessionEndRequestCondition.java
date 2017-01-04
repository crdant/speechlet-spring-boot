package io.crdant.spring.alexa.speechlet.handler.condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class SessionEndRequestCondition extends AbstractSpeechletRequestCondition<SessionEndRequestCondition> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Set<String> applicationIds;

    public SessionEndRequestCondition(String... applicationIds) {
        this.applicationIds = new LinkedHashSet<String>() ;
        this.applicationIds.addAll(Arrays.asList(applicationIds));
    }

    public SessionEndRequestCondition(Collection<String> applicationIds) {
        this.applicationIds = new LinkedHashSet<String>() ;
        this.applicationIds.addAll(applicationIds) ;
    }

    @Override
    protected Collection<?> getContent() {
        return null;
    }

    @Override
    public SessionEndRequestCondition combine(SessionEndRequestCondition other) {
        return null;
    }

    @Override
    public SessionEndRequestCondition getMatchingCondition(HttpServletRequest request) {
        return null;
    }

    @Override
    public int compareTo(SessionEndRequestCondition other, HttpServletRequest request) {
        return 0;
    }
}
