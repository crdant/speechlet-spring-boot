package io.crdant.spring.alexa.condition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.crdant.spring.alexa.util.RequestUtils;
import org.apache.http.entity.ContentType;
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

public class IntentRequestCondition implements RequestCondition<IntentRequestCondition> {

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
    public IntentRequestCondition getMatchingCondition(HttpServletRequest request) {
        logger.debug("Seeing if the condition matches for intent " + intents.iterator().next());
        IntentRequestCondition condition = null ;
        try {
            // if it's not json then it's not an Alexa request
            if ( !request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) ) return condition ;
            String intent = getRequestIntent(request) ;
            if (  intent != null ) {
                for ( String mapped  : this.intents ) {
                    if ( mapped.equalsIgnoreCase(intent) ) {
                        condition = this ;
                    }
                }
            }
        } catch ( Exception e) {
            logger.error("Error checking for Alexa intent", e);
        }
        return condition ;
    }

    private String getRequestIntent( HttpServletRequest request ) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonRoot = RequestUtils.getRequestJson(WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class));
            JsonNode alexaRequest = jsonRoot.get("request");
            if (alexaRequest != null) {
                if (alexaRequest.get("type") != null && alexaRequest.get("type").asText().equals("IntentRequest")) {
                    JsonNode intent = alexaRequest.get("intent");
                    if (intent != null) {
                        return intent.get("name").asText();
                    }
                }
            }
        } catch ( IOException ioE ) {
            logger.error("Error reading the body from the request, can't determine intent");
        }
        return null ;
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

}