package io.crdant.spring.alexa.speechlet.handler;

import io.crdant.spring.alexa.annotation.Speechlet;
import io.crdant.spring.alexa.speechlet.method.SpeechletMappingInfo;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;

public class SpeechletHandlerMapping extends AbstractHandlerMethodMapping<SpeechletMappingInfo> {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return AnnotatedElementUtils.hasAnnotation(beanType, Speechlet.class);
    }

    @Override
    protected SpeechletMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        return null;
    }

    /**
     * This will always return null, since Speechlets are mapped based on the content of the request rather
     * than the path.
     *
     * @param mapping
     * @return
     */
    @Override
    protected Set<String> getMappingPathPatterns(SpeechletMappingInfo mapping) {
        // paths are irrelevant to a speechlet, we are basing our mappings on the body of the request
        return null;
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
