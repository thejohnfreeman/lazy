package com.thejohnfreeman.function;

/**
 * A function of six arguments.
 *
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @param <C> the type of the third argument
 * @param <D> the type of the fourth argument
 * @param <E> the type of the fifth argument
 * @param <F> the type of the sixth argument
 * @param <R> the type of the result
 */
@FunctionalInterface
public interface Function6<A, B, C, D, E, F, R>
{
    R apply(A a, B b, C c, D d, E e, F f);
}
