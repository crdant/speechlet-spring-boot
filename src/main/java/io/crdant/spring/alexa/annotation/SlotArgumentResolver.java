package io.crdant.spring.alexa.annotation;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletRequest;
import io.crdant.spring.alexa.speechlet.web.SpeechletServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        Object value = null ;
        Set<String> intents = new HashSet();
        intents.addAll(Arrays.asList(parameter.getMethodAnnotation(IntentMapping.class).intent()));
        String slot = parameter.getParameterAnnotation(Slot.class).value();
        SpeechletServletRequest request = (SpeechletServletRequest) webRequest.getNativeRequest(SpeechletServletRequest.class);
        if ( request != null ) {
            SpeechletRequest speechletRequest = request.getSpeechletRequest(IntentRequest.class);
            if (speechletRequest != null ) {
                Intent intent = ((IntentRequest)speechletRequest).getIntent();
                if ( intents.contains(intent.getName()) && intent.getSlot(slot) != null ) {
                    value = intent.getSlot(slot).getValue();
                }
            }
        }
        return value ;
    }

}