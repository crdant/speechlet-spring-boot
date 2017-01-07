package io.crdant.spring.speechlet.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface SessionEnded {
    /**
     * Assign a name to this mapping.
     */
    String name() default "";

}
