package com.jfreeman.lazy;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.ImmutableList;

/**
 * A lazy value with two dependencies.
 *
 * @param <T> the type of the value.
 * @param <A> the type of the first dependency.
 * @param <B> the type of the second dependency.
 * @author jfreeman
 */
class BiThunk<T, A, B>
    implements Lazy<T>
{
    private final Lazy<A> _depA;
    private final Lazy<B> _depB;
    private BiFunction<A, B, T> _func;
    private T _value = null;

    private BiThunk(Lazy<A> a, Lazy<B> b, BiFunction<A, B, T> func) {
        _depA = a;
        _depB = b;
        _func = func;
    }

    public static <T, A, B> BiThunk<T, A, B> create(
        Lazy<A> a, Lazy<B> b, BiFunction<A, B, T> func)
    {
        return new BiThunk<>(a, b, func);
    }

    @Override
    public List<Lazy<?>> getDependencies() {
        return ImmutableList.of(_depA, _depB);
    }

    @Override
    public T getValue() throws IllegalStateException {
        if (!isForced()) {
            throw new IllegalStateException("not yet forced");
        }
        return _value;
    }

    @Override
    public T force() throws IllegalStateException {
        if (isForced()) {
            throw new IllegalStateException("already forced");
        }
        final A a = _depA.getValue();
        final B b = _depB.getValue();
        _value = _func.apply(a, b);
        _func = null;
        return _value;
    }

    @Override
    public boolean isForced() {
        return _func == null;
    }
}
