package io.crdant.spring.alexa.speechlet.handler;

import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;

import io.crdant.spring.alexa.speechlet.method.SpeechletMappingInfo;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;

public class SpeechletHandlerMapping extends AbstractHandlerMethodMapping<SpeechletMappingInfo> {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return false;
    }

    @Override
    protected SpeechletMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        return null;
    }

    @Override
    protected Set<String> getMappingPathPatterns(SpeechletMappingInfo mapping) {
        return null;
    }

    @Override
    protected SpeechletMappingInfo getMatchingMapping(SpeechletMappingInfo mapping, HttpServletRequest request) {
        return null;
    }

    @Override
    protected Comparator<SpeechletMappingInfo> getMappingComparator(HttpServletRequest request) {
        return null;
    }
}
