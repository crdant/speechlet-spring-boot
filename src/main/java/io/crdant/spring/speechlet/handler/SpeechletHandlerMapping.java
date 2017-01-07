package io.crdant.spring.speechlet.handler;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.Session;
import io.crdant.spring.speechlet.annotation.*;
import io.crdant.spring.speechlet.handler.condition.SessionStartedRequestCondition;
import io.crdant.spring.speechlet.method.SpeechletMappingInfo;
import io.crdant.spring.speechlet.web.SpeechletServletRequest;
import io.crdant.spring.speechlet.handler.condition.IntentRequestCondition;
import io.crdant.spring.speechlet.handler.condition.LaunchRequestCondition;
import io.crdant.spring.speechlet.handler.condition.SessionEndedRequestCondition;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SpeechletHandlerMapping extends AbstractHandlerMethodMapping<SpeechletMappingInfo> {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return AnnotatedElementUtils.hasAnnotation(beanType, Speechlet.class) || AnnotatedElementUtils.hasAnnotation(beanType, IntentMapping.class) ||
                AnnotatedElementUtils.hasAnnotation(beanType, Launch.class) || AnnotatedElementUtils.hasAnnotation(beanType, SessionStart.class) ||
                AnnotatedElementUtils.hasAnnotation(beanType, SessionEnd.class) ;
    }


    @Override
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
        SpeechletServletRequest speechletServletRequest = (SpeechletServletRequest) request ;
        logger.debug("looking up handler method for path " + lookupPath + ", request path " + request.getPathTranslated() +
                        ", request context path " + speechletServletRequest.getContextPath() +
                        ", speechlet context " + speechletServletRequest.getSpeechletContext() +
                        ", speechlet session " + speechletServletRequest.getSpeechletSession() +
                        ", speechlet request " + speechletServletRequest.getSpeechletRequest());
        logger.debug("speechletServletRequest: " + new String(speechletServletRequest.getSerializedRequest()));
        String derivedPath = lookupPath + "/" + speechletServletRequest.getApplicationId();
        if ( speechletServletRequest.isIntentRequest() ) lookupPath = lookupPath + "/" + speechletServletRequest.getIntentName();
        if ( speechletServletRequest.isLaunchRequest() ) lookupPath = lookupPath + "/launch";
        if ( speechletServletRequest.isSessionStartedRequest() ) lookupPath = lookupPath + "/sessionStart";
        if ( speechletServletRequest.isSessionEndedRequest()  ) lookupPath = lookupPath + "/sessionEnd";
        return super.lookupHandlerMethod(derivedPath, request);
    }

    @Override
    protected SpeechletMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        return createRequestMappingInfo(method, handlerType);
    }

    private SpeechletMappingInfo createRequestMappingInfo(Method method, Class<?> typeElement ) {
        String name = "";
        boolean handler = false ;
        String applicationId = null ;
        LaunchRequestCondition launchRequestCondition = null ;
        SessionStartedRequestCondition sessionStartedRequestCondition = null ;
        IntentRequestCondition intentRequestCondition = null ;
        SessionEndedRequestCondition sessionEndedRequestCondition = null ;

        Speechlet speechlet = AnnotatedElementUtils.findMergedAnnotation(typeElement, Speechlet.class);
        if (speechlet != null) {
            name = name + "/" + speechlet.value()[0];
            applicationId = speechlet.value()[0];

            Launch launch = AnnotatedElementUtils.findMergedAnnotation(method, Launch.class);
            if (launch != null || method.getName().equals("onLaunch")) {
                handler = true;
                launchRequestCondition = new LaunchRequestCondition(applicationId);
            }

            SessionStart start = AnnotatedElementUtils.findMergedAnnotation(method, SessionStart.class);
            if (start != null || method.getName().equals("onSessionStart")) {
                handler = true;
                sessionStartedRequestCondition = new SessionStartedRequestCondition(applicationId);
            }

            IntentMapping intent = AnnotatedElementUtils.findMergedAnnotation(method, IntentMapping.class);
            System.out.println("Working with method " + method.getClass().getCanonicalName() + "." + method.getName());
            if (intent != null || method.getName().equals("onIntent")) {
                handler = true;
                name = name + "/" + intent.value()[0];
                intentRequestCondition = new IntentRequestCondition(applicationId, intent.value());
            }

            SessionEnd end = AnnotatedElementUtils.findMergedAnnotation(method, SessionEnd.class);
            if (end != null || method.getName().equals("onSessionEnd")) {
                handler = true;
                name = name + "/" + "sessionEnd";
                sessionEndedRequestCondition = new SessionEndedRequestCondition(applicationId);
            }

            if (handler) {
                return new SpeechletMappingInfo(name, launchRequestCondition, sessionStartedRequestCondition,
                        intentRequestCondition, sessionEndedRequestCondition);
            }
        }
        return null ;

    }

    /**
     * Speechlets are mapped based on the content of the request rather than the path, so we will always return
     * a pattern that matches all.
     *
     * @param mapping
     * @return
     */
    @Override
    protected Set<String> getMappingPathPatterns(SpeechletMappingInfo mapping) {
        // paths are irrelevant to a speechlet, we are basing our mappings on the body of the request
        Set<String> patterns = new HashSet<String>();
        patterns.add("/**/*");
        return patterns;
    }

    @Override
    protected SpeechletMappingInfo getMatchingMapping(SpeechletMappingInfo mapping, HttpServletRequest request) {
        return mapping.getMatchingCondition(request);
    }

    @Override
    protected Comparator<SpeechletMappingInfo> getMappingComparator(HttpServletRequest request) {
        return new Comparator<SpeechletMappingInfo>() {
            @Override
            public int compare(SpeechletMappingInfo first, SpeechletMappingInfo second) {
                return first.compareTo(second, request);
            }
        };
    }
}
