package com.shootforever.nuclear.event;

import java.lang.annotation.*;

/**
 * Annotation used to mark a method as an event handling method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {
}
