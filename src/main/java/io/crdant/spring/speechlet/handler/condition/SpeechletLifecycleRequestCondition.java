package io.crdant.spring.speechlet.handler.condition;

import com.amazon.speech.speechlet.Application;
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

public abstract class SpeechletLifecycleRequestCondition<T extends SpeechletLifecycleRequestCondition<T>> extends AbstractSpeechletRequestCondition<T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Set<String> applicationIds;

    public SpeechletLifecycleRequestCondition() {
        super();
        this.applicationIds = new LinkedHashSet<>();
    }

    public SpeechletLifecycleRequestCondition(String... applicationIds) {
        this.applicationIds = new LinkedHashSet<String>() ;
        this.applicationIds.addAll(Arrays.asList(applicationIds));
    }

    public SpeechletLifecycleRequestCondition(Collection<String> applicationIds) {
        this.applicationIds = new LinkedHashSet<String>() ;
        this.applicationIds.addAll(applicationIds) ;
    }

    public Set<String> getApplicationIds() {
        return applicationIds;
    }

    protected T getMatchingConditionInternal(Context speechletContext, Session speechletSession, SpeechletRequest speechletRequest) {
        T condition = null ;
        Application application = speechletSession.getApplication();
        if ( application.getApplicationId() != null ) {
            for ( String mapped : this.applicationIds ) {
                if ( mapped.equals(application.getApplicationId()) ) {
                    condition = (T) this ;
                }
            }
        }
        return condition;
    }

    @Override
    protected Collection<?> getContent() {
        return applicationIds;
    }

    @Override
    public T combine(T other) {
        Set<String> applicationIds = new LinkedHashSet<String>(this.applicationIds);
        applicationIds.addAll(((SpeechletLifecycleRequestCondition<T>) other).applicationIds);
        return newInstance(applicationIds);
    }

    abstract protected T newInstance(Set<String> applicationIds) ;

    @Override
    public int compareTo(T other, HttpServletRequest request) {
        int same = 0;
        if ( ! ( other.getClass().isAssignableFrom(this.getClass()) ) ) return -1 ;

        for ( String id : other.getApplicationIds() ) {
            if ( !this.applicationIds.contains(id) ) return -1 ;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "SpeechletLifecycleRequestCondition{" +
                "applicationIds=" + applicationIds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SpeechletLifecycleRequestCondition<?> that = (SpeechletLifecycleRequestCondition<?>) o;

        return applicationIds != null ? applicationIds.equals(that.applicationIds) : that.applicationIds == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (applicationIds != null ? applicationIds.hashCode() : 0);
        return result;
    }
}