package io.crdant.spring.speechlet.handler;

import io.crdant.spring.speechlet.annotation.*;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SpeechletHandlerMethodAdapter extends AbstractHandlerMethodAdapter {


    @Override
    protected boolean supportsInternal(HandlerMethod handlerMethod) {
        logger.debug("called supportsInternal for method " + handlerMethod.getMethod().getName() );
        boolean result =  AnnotatedElementUtils.hasAnnotation(handlerMethod.getClass(), Speechlet.class) &&
                ( handlerMethod.hasMethodAnnotation(IntentMapping.class) || handlerMethod.hasMethodAnnotation(Launch.class) ||
                    handlerMethod.hasMethodAnnotation(SessionStart.class) || handlerMethod.hasMethodAnnotation(SessionEnd.class) );
        logger.debug("result is " + result);
        return result ;
    }

    @Override
    protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        logger.debug("called handlerInternal");
        return null;
    }

    @Override
    protected long getLastModifiedInternal(HttpServletRequest request, HandlerMethod handlerMethod) {
        logger.debug("called getLastModifiedInternal");
        return 0;
    }
}
