package io.crdant.spring.alexa.speechlet.handler.condition;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.crdant.spring.alexa.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class IntentRequestCondition extends AbstractSpeechletRequestCondition<IntentRequestCondition> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Set<String> intents ;

    public IntentRequestCondition(String... intents) {
        this.intents = new LinkedHashSet<String>() ;
        this.intents.addAll(Arrays.asList(intents));
    }

    public IntentRequestCondition(Collection<String> intents) {
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
        Set<String> intents = new LinkedHashSet<String>(this.intents);
        intents.addAll(((IntentRequestCondition) other).intents);
        return new IntentRequestCondition(intents);
    }

    @Override
    public int compareTo(IntentRequestCondition other, HttpServletRequest request) {
        int same = 0;
        for ( String intent : other.intents ) {
            if ( !this.intents.contains(intent) ) return -1 ;
        }
        return 0;
    }

    @Override
    protected Collection<?> getContent() {
        return intents ;
    }
}