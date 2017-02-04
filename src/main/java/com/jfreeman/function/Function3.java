package com.jfreeman.function;

/**
 * A function of three arguments.
 *
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @param <C> the type of the third argument
 * @param <R> the type of the result
 */
@FunctionalInterface
public interface Function3<A, B, C, R>
{
    R apply(A a, B b, C c);
}
