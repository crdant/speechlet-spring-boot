package io.crdant.tfinder.web.method;

import io.crdant.tfinder.bind.annotation.IntentController;
import io.crdant.tfinder.bind.annotation.IntentMapping;
import io.crdant.tfinder.condition.IntentRequestCondition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class IntentRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        IntentController typeAnnotation = AnnotationUtils.findAnnotation(handlerType, IntentController.class);
        logger.debug("Searched for class annotation @IntentContoller on " + handlerType.getName() + " and found " + typeAnnotation);
        return (typeAnnotation != null) ? new IntentRequestCondition() : null;
    }

    @Override protected RequestCondition<?> getCustomMethodCondition(Method handlerMethod) {
        IntentMapping methodAnnotation = AnnotationUtils.findAnnotation(handlerMethod, IntentMapping.class);
        logger.debug("Searched for method annotation @IntentMapping on " + handlerMethod.getName() + " and found " + methodAnnotation);
        return (methodAnnotation != null) ? new IntentRequestCondition( methodAnnotation.value() ) : null;
    }
}
