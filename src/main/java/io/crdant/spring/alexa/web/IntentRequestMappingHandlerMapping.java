package io.crdant.spring.alexa.web;

import io.crdant.spring.alexa.condition.IntentRequestCondition;
import io.crdant.spring.alexa.web.annotation.IntentMapping;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.MatchableHandlerMapping;
import org.springframework.web.servlet.handler.RequestMatchResult;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class IntentRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) ||
                AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class) ||
                AnnotatedElementUtils.hasAnnotation(beanType, IntentMapping.class));
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method handlerMethod) {
        IntentMapping methodAnnotation = AnnotationUtils.findAnnotation(handlerMethod, IntentMapping.class);
        return (methodAnnotation != null) ? new IntentRequestCondition(methodAnnotation.value()) : super.getCustomMethodCondition(handlerMethod);
    }

}