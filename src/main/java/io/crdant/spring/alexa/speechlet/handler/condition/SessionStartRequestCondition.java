package io.crdant.spring.alexa.speechlet.handler.condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class SessionStartRequestCondition extends AbstractSpeechletRequestCondition<SessionStartRequestCondition> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Set<String> applicationIds;

    public SessionStartRequestCondition(String... applicationIds) {
        this.applicationIds = new LinkedHashSet<String>() ;
        this.applicationIds.addAll(Arrays.asList(applicationIds));
    }

    public SessionStartRequestCondition(Collection<String> applicationIds) {
        this.applicationIds = new LinkedHashSet<String>() ;
        this.applicationIds.addAll(applicationIds) ;
    }

    @Override
    protected Collection<?> getContent() {
        return null;
    }

    @Override
    public SessionStartRequestCondition combine(SessionStartRequestCondition other) {
        return null;
    }

    @Override
    public SessionStartRequestCondition getMatchingCondition(HttpServletRequest request) {
        return null;
    }

    @Override
    public int compareTo(SessionStartRequestCondition other, HttpServletRequest request) {
        return 0;
    }
}
