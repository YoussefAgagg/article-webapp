package com.example.articlewebapp.aop.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A custom annotation as a pointcut, thus wherever this annotation appears on a method, logging
 * aspect will run around that method execution and log the entry-exit and time taken by that
 * method.
 *
 * @author Youssef Agagg
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {}
