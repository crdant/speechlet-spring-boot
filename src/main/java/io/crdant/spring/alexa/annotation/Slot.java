package io.crdant.spring.alexa.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Slot {

    /**
     * Alias for {@link #name}.
     */
    @AliasFor("name")
    String value() default "";

    /**
     * The name of the slot to bind to.
     * @since 4.2
     */
    @AliasFor("value")
    String name() default "";

    /**
     * Whether the slot is required.
     * <p>Defaults to {@code true}, leading to an exception being thrown
     * if the slot is missing in the intent. Switch this to
     * {@code false} if you prefer a {@code null} value if the slot is
     * not present in the intent.
     * <p>Alternatively, provide a {@link #defaultValue}, which implicitly
     * sets this flag to {@code false}.
     */
    boolean required() default true;

    /**
     * The default value to use as a fallback when the slot is
     * not provided or has an empty value.
     * <p>Supplying a default value implicitly sets {@link #required} to
     * {@code false}.
     */
    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
