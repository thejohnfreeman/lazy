package com.jfreeman.lazy;

import java.util.List;

import com.google.common.collect.ImmutableList;

import com.jfreeman.function.TriFunction;

/**
 * A lazy value with three dependencies.
 *
 * @param <T> the type of the value.
 * @param <A> the type of the first dependency.
 * @param <B> the type of the second dependency.
 * @param <C> the type of the third dependency.
 * @author jfreeman
 */
class TriThunk<T, A, B, C>
    implements Lazy<T>
{
    private final Lazy<A> _depA;
    private final Lazy<B> _depB;
    private final Lazy<C> _depC;
    private TriFunction<A, B, C, T> _func;
    private T _value = null;

    private TriThunk(
        Lazy<A> a, Lazy<B> b, Lazy<C> c, TriFunction<A, B, C, T> func)
    {
        _depA = a;
        _depB = b;
        _depC = c;
        _func = func;
    }

    public static <T, A, B, C> TriThunk<T, A, B, C> create(
        Lazy<A> a, Lazy<B> b, Lazy<C> c, TriFunction<A, B, C, T> func)
    {
        return new TriThunk<>(a, b, c, func);
    }

    @Override
    public List<Lazy<?>> getDependencies() {
        return ImmutableList.of(_depA, _depB, _depC);
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
        final C c = _depC.getValue();
        _value = _func.apply(a, b, c);
        _func = null;
        return _value;
    }

    @Override
    public boolean isForced() {
        return _func == null;
    }
}
