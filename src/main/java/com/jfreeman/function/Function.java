package com.jfreeman.function;

/**
 * A function of a single argument.
 *
 * @param <T> the type of the argument.
 * @param <R> the type of the result.
 * @author jfreeman
 */
public interface Function<T, R> {
    R apply(T t);
}
