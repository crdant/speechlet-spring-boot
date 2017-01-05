package io.crdant.spring.alexa.speechlet.handler;

import io.crdant.spring.alexa.annotation.*;
import io.crdant.spring.alexa.speechlet.handler.condition.*;
import io.crdant.spring.alexa.speechlet.method.SpeechletMappingInfo;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SpeechletHandlerMapping extends AbstractHandlerMethodMapping<SpeechletMappingInfo> {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return AnnotatedElementUtils.hasAnnotation(beanType, Speechlet.class) ||
                AnnotatedElementUtils.hasAnnotation(beanType, IntentMapping.class) ||
                AnnotatedElementUtils.hasAnnotation(beanType, Launch.class) ||
                AnnotatedElementUtils.hasAnnotation(beanType, SessionStart.class) ||
                AnnotatedElementUtils.hasAnnotation(beanType, SessionEnd.class) ;
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
            name = name + "/" + speechlet.value();
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
            if (intent != null || method.getName().equals("onIntent")) {
                handler = true;
                name = name + "/" + intent.value();
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
        return null;
    }
}
