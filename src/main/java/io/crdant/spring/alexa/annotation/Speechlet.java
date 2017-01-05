package io.crdant.spring.alexa.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
public @interface Speechlet {
    /**
     * Assign a name to this mapping.
     */
    String name() default "";

    /**
     * The Speechlet mapping expressed by this annotation.
     * <p>This is an alias for {@link #applicationId}. For example
     * {@code @Speechlet("/foo")} is equivalent to
     * {@code @Speechlet(intent="/foo")}.
     */
    @AliasFor("applicationId")
    String[] value() default {};

    /**
     * The id of the Amazon Alexa skill application that the class services.
     */
    @AliasFor("value")
    String[] applicationId() default {};
}
