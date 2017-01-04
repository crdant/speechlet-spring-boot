package io.crdant.spring.alexa.speechlet.handler.condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class LaunchRequestCondition extends AbstractSpeechletRequestCondition<LaunchRequestCondition> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Set<String> applicationIds;

    public LaunchRequestCondition(String... applicationIds) {
        this.applicationIds = new LinkedHashSet<String>() ;
        this.applicationIds.addAll(Arrays.asList(applicationIds));
    }

    public LaunchRequestCondition(Collection<String> applicationIds) {
        this.applicationIds = new LinkedHashSet<String>() ;
        this.applicationIds.addAll(applicationIds) ;
    }

    @Override
    protected Collection<?> getContent() {
        return null;
    }

    @Override
    public LaunchRequestCondition combine(LaunchRequestCondition other) {
        return null;
    }

    @Override
    public LaunchRequestCondition getMatchingCondition(HttpServletRequest request) {
        return null;
    }

    @Override
    public int compareTo(LaunchRequestCondition other, HttpServletRequest request) {
        return 0;
    }
}
