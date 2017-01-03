package io.crdant.spring.alexa.web.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface IntentMapping {
    /**
     * Assign a name to this mapping.
     */
    String name() default "";

    /**
     * The primary mapping expressed by this annotation.
     * <p>This is an alias for {@link #intent}. For example
     * {@code @IntentMapping("/foo")} is equivalent to
     * {@code @IntentMapping(intent="/foo")}.
     */
    @AliasFor("intent")
    String[] value() default {};

    /**
     * The name of the Amazon Alexa intent that the method services.
     */
    @AliasFor("value")
    String[] intent() default {};
}
