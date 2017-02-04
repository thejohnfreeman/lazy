package com.jfreeman.lazy;

import java.util.function.BiFunction;

import com.google.common.collect.ImmutableList;

/**
 * A lazy value computed from two dependencies.
 *
 * @param <T> the type of the value
 * @param <A> the type of the first dependency
 * @param <B> the type of the second dependency
 */
public final class Thunk2<T, A, B>
    extends AbstractThunk<T>
{
    private Lazy<A> _depA;
    private Lazy<B> _depB;
    private BiFunction<A, B, T> _func;

    private Thunk2(Lazy<A> a, Lazy<B> b, BiFunction<A, B, T> func) {
        _depA = a;
        _depB = b;
        _func = func;
    }

    public static <T, A, B> Thunk2<T, A, B> of(
        Lazy<A> a, Lazy<B> b, BiFunction<A, B, T> func)
    {
        return new Thunk2<>(a, b, func);
    }

    @Override
    public boolean isForced() {
        return _func == null;
    }

    @Override
    public Iterable<? extends Lazy<?>> getDependencies()
        throws IllegalStateException
    {
        if (isForced()) {
            throw new IllegalStateException("already forced");
        }
        return ImmutableList.of(_depA, _depB);
    }

    @Override
    public T force()
        throws IllegalStateException
    {
        if (isForced()) {
            throw new IllegalStateException("already forced");
        }
        final A a = _depA.getValue();
        final B b = _depB.getValue();
        _value = _func.apply(a, b);
        _func = null;
        _depA = null;
        _depB = null;
        return _value;
    }

    @Override
    public String toStringUnforced(final String name) {
        return "(_2_) -> " + name;
    }
}
