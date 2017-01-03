package io.crdant.spring.alexa.condition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.crdant.spring.alexa.util.RequestUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
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
        IntentRequestCondition condition = null ;
        System.out.println("request actual type: " + request.getClass().getCanonicalName());
        try {
            HttpInputMessage x = (HttpInputMessage) request ;
            // if it's not json then it's not an Alexa request
            if ( !request.getContentType().equals(ContentType.APPLICATION_JSON) ) return condition ;
            logger.debug("Validating if the request contains an Alexa intent");
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
                logger.debug("JSON request contains the 'request' field, looking for intent");
                if (alexaRequest.get("type") != null && alexaRequest.get("type").equals("IntentRequest")) {
                    logger.debug("Request is an intent request");
                    JsonNode intent = alexaRequest.get("type").get("intent");
                    if (intent != null) {
                        logger.debug("Request is for intent: " + intent.asText());
                        return intent.asText();
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
        intents.addAll(other.intents);
        return new IntentRequestCondition(intents);
    }

    @Override
    public int compareTo(IntentRequestCondition other, HttpServletRequest request) {
        return (other.intents.size() - this.intents.size());
    }

}