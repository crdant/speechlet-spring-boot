package io.crdant.spring.alexa.annotation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.crdant.spring.alexa.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SlotArgumentResolver implements HandlerMethodArgumentResolver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        logger.debug("Determining if we support parameter " + parameter.getParameterName());
        return parameter.getParameterAnnotation(Slot.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        logger.debug("Attempting to resolve a slot into a parameter for the incoming request");
        String intent = parameter.getMethodAnnotation(IntentMapping.class).intent()[0];
        String slot = parameter.getParameterAnnotation(Slot.class).value();
        logger.debug("Getting the value for slot " + slot + " on request for intent " + intent );
        return getSlotValue(intent, slot, webRequest);
    }

    private String getSlotValue( String intent, String slot, NativeWebRequest request ) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonRoot = RequestUtils.getRequestJson(request);
            JsonNode alexaRequest = jsonRoot.get("request");
            if (alexaRequest != null) {
                logger.debug("JSON request contains the 'request' field, looking for intent");
                if (alexaRequest.get("type") != null && alexaRequest.get("type").asText().equals("IntentRequest")) {
                    logger.debug("Request is an intent request");
                    JsonNode intentNode = alexaRequest.get("intent");
                    if (intentNode != null) {
                        logger.debug("Comparing request intent " + intentNode.get("name").asText() + " to intent " + intent);
                        if ( intentNode.get("name").asText().equals(intent) ) {
                            JsonNode slots = intentNode.get("slots");
                            logger.debug("Slots: " + slots);
                            if ( slots.get(slot) == null ) return null ;
                            return slots.get(slot).get("value").asText();
                        }
                    }
                }
            }
        } catch ( Exception ioE ) {
            logger.error("Error reading the body from the request, can't read slots");
        }
        return null ;
    }

}