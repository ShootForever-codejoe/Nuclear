package com.shootforever.nuclear.event;

import java.lang.annotation.*;

/**
 * Annotation used to mark a method as an event handling method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {
    /**
     * The priority value of the event handling method. Methods with lower values will be executed first.
     *
     * @return The priority value, with a default of 10 if not specified.
     */
    int priority() default 10;
}
