package io.crdant.spring.speechlet.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface Launch {
    /**
     * Assign a name to this mapping.
     */
    public String name() default "";

}
