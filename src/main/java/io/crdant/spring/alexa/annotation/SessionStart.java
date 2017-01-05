package io.crdant.spring.alexa.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface SessionStart {
    /**
     * Assign a name to this mapping.
     */
    String name() default "";

}
