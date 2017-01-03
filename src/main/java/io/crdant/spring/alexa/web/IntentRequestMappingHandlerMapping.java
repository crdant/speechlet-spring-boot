package io.crdant.spring.alexa.web;

import io.crdant.spring.alexa.condition.IntentRequestCondition;
import io.crdant.spring.alexa.web.annotation.IntentMapping;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

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
        logger.debug("Searched for method annotation @IntentMapping on " + handlerMethod.getName() + " and found " + methodAnnotation);
        return (methodAnnotation != null) ? new IntentRequestCondition(methodAnnotation.value()) : super.getCustomMethodCondition(handlerMethod);
    }

}