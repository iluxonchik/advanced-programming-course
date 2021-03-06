package pva.lab03.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * pva.lab03.annotations.Test annotation definition.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    String SETUP_ALL = "*";
    String[] value() default {SETUP_ALL};
}