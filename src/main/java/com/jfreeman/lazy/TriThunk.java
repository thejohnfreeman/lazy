package com.jfreeman.lazy;

import com.google.common.collect.ImmutableList;

import com.jfreeman.function.TriFunction;

/**
 * A lazy value with three dependencies.
 *
 * @param <T> the type of the value
 * @param <A> the type of the first dependency
 * @param <B> the type of the second dependency
 * @param <C> the type of the third dependency
 * @author jfreeman
 */
public final class TriThunk<T, A, B, C>
    extends AbstractThunk<T>
{
    private Lazy<A> _depA;
    private Lazy<B> _depB;
    private Lazy<C> _depC;
    /** @see Thunk#_func */
    private TriFunction<A, B, C, T> _func;

    private TriThunk(
        Lazy<A> a, Lazy<B> b, Lazy<C> c, TriFunction<A, B, C, T> func)
    {
        _depA = a;
        _depB = b;
        _depC = c;
        _func = func;
    }

    public static <T, A, B, C> TriThunk<T, A, B, C> of(
        Lazy<A> a, Lazy<B> b, Lazy<C> c, TriFunction<A, B, C, T> func)
    {
        return new TriThunk<>(a, b, c, func);
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
        return ImmutableList.of(_depA, _depB, _depC);
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
        final C c = _depC.getValue();
        _value = _func.apply(a, b, c);
        _func = null;
        _depA = null;
        _depB = null;
        _depC = null;
        return _value;
    }
}
