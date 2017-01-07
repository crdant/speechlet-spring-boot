package io.crdant.spring.speechlet.handler.condition;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class IntentRequestCondition extends AbstractSpeechletRequestCondition<IntentRequestCondition> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String applicationId ;
    private final Set<String> intents ;

    public IntentRequestCondition() {
        this.applicationId = "" ;
        this.intents = new LinkedHashSet<String>() ;
    }

    public IntentRequestCondition(String applicationId, String... intents) {
        this.applicationId = applicationId ;
        this.intents = new LinkedHashSet<String>() ;
        this.intents.addAll(Arrays.asList(intents));
    }

    public IntentRequestCondition(String applicationId, Collection<String> intents) {
        this.applicationId = applicationId ;
        this.intents = new LinkedHashSet<String>() ;
        intents.addAll(intents) ;
    }

    @Override
    protected IntentRequestCondition getMatchingConditionInternal(Context speechletContext, Session speechletSession, SpeechletRequest speechletRequest) {
        IntentRequestCondition condition = null ;
        if ( !(speechletRequest instanceof IntentRequest ) ) return condition ;
        IntentRequest intentRequest = (IntentRequest) speechletRequest ;
        Intent intent = intentRequest.getIntent();
        if (  intent != null ) {
            for ( String mapped  : this.intents ) {
                if ( mapped.equalsIgnoreCase(intent.getName()) ) {
                    condition = this ;
                }
            }
        }
        return condition;
    }

    @Override
    public IntentRequestCondition combine(IntentRequestCondition other) {
        // TO DO: be more elegant
        if ( this.applicationId != other.applicationId ) return null ;
        Set<String> intents = new LinkedHashSet<String>(this.intents);
        intents.addAll(((IntentRequestCondition) other).intents);
        return new IntentRequestCondition(applicationId, intents);
    }

    @Override
    public int compareTo(IntentRequestCondition other, HttpServletRequest request) {
        int same = 0;
        if ( !this.applicationId.equals(other.applicationId )) return -1 ;

        for ( String intent : other.intents ) {
            if ( !this.intents.contains(intent) ) return -1 ;
        }
        return 0;
    }

    public String getApplicationId() {
        return applicationId;
    }

    @Override
    protected Collection<?> getContent() {
        return this.intents ;
    }

    @Override
    public String toString() {
        return "IntentRequestCondition{" +
                "applicationId=" + applicationId +
                ", intents=" + intents +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        IntentRequestCondition that = (IntentRequestCondition) o;

        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null)
            return false;
        return intents != null ? intents.equals(that.intents) : that.intents == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (applicationId != null ? applicationId.hashCode() : 0);
        result = 31 * result + (intents != null ? intents.hashCode() : 0);
        return result;
    }
}