package io.crdant.spring.alexa.speechlet.handler.condition;

import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import java.util.Collection;

public abstract class AbstractSpeechletRequestCondition<T extends AbstractSpeechletRequestCondition<T>> extends AbstractRequestCondition<T> {

    @Override
    protected String getToStringInfix() {
        return " || ";
    }
}
