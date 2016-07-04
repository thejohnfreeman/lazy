package com.jfreeman.lazy;

import java.util.List;

import com.google.common.collect.ImmutableList;

import com.jfreeman.function.QuadFunction;

/**
 * A lazy value with four dependencies.
 *
 * @param <T> the type of the value
 * @param <A> the type of the first dependency
 * @param <B> the type of the second dependency
 * @param <C> the type of the third dependency
 * @param <D> the type of the fourth dependency
 * @author jfreeman
 */
public class QuadThunk<T, A, B, C, D>
    implements Lazy<T>
{
    private Lazy<A> _depA;
    private Lazy<B> _depB;
    private Lazy<C> _depC;
    private Lazy<D> _depD;
    private QuadFunction<A, B, C, D, T> _func;
    private T _value = null;

    private QuadThunk(
        Lazy<A> a, Lazy<B> b, Lazy<C> c, Lazy<D> d,
        QuadFunction<A, B, C, D, T> func)
    {
        _depA = a;
        _depB = b;
        _depC = c;
        _depD = d;
        _func = func;
    }

    public static <T, A, B, C, D> QuadThunk<T, A, B, C, D> of(
        Lazy<A> a, Lazy<B> b, Lazy<C> c, Lazy<D> d,
        QuadFunction<A, B, C, D, T> func)
    {
        return new QuadThunk<>(a, b, c, d, func);
    }

    @Override
    public boolean isForced() {
        return _func == null;
    }

    @Override
    public List<Lazy<?>> getDependencies()
        throws IllegalStateException
    {
        if (isForced()) {
            throw new IllegalStateException("already forced");
        }
        return ImmutableList.of(_depA, _depB, _depC, _depD);
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
        final D d = _depD.getValue();
        _value = _func.apply(a, b, c, d);
        _func = null;
        _depA = null;
        _depB = null;
        _depC = null;
        _depD = null;
        return _value;
    }

    @Override
    public T getValue()
        throws IllegalStateException
    {
        if (!isForced()) {
            throw new IllegalStateException("not yet forced");
        }
        return _value;
    }
}
