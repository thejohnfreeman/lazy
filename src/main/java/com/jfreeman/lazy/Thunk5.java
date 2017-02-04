package com.jfreeman.lazy;

import com.google.common.collect.ImmutableList;

import com.jfreeman.function.Function5;

/**
 * A lazy value computed from five dependencies.
 *
 * @param <T> the type of the value
 * @param <A> the type of the first dependency
 * @param <B> the type of the second dependency
 * @param <C> the type of the third dependency
 * @param <D> the type of the fourth dependency
 * @param <E> the type of the fifth dependency
 */
public final class Thunk5<T, A, B, C, D, E>
    extends AbstractThunk<T>
{
    private Lazy<A> _depA;
    private Lazy<B> _depB;
    private Lazy<C> _depC;
    private Lazy<D> _depD;
    private Lazy<E> _depE;
    private Function5<A, B, C, D, E, T> _func;

    private Thunk5(
        Lazy<A> a, Lazy<B> b, Lazy<C> c, Lazy<D> d, Lazy<E> e,
        Function5<A, B, C, D, E, T> func)
    {
        _depA = a;
        _depB = b;
        _depC = c;
        _depD = d;
        _depE = e;
        _func = func;
    }

    public static <T, A, B, C, D, E> Thunk5<T, A, B, C, D, E> of(
        Lazy<A> a, Lazy<B> b, Lazy<C> c, Lazy<D> d, Lazy<E> e,
        Function5<A, B, C, D, E, T> func)
    {
        return new Thunk5<>(a, b, c, d, e, func);
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
        return ImmutableList.of(_depA, _depB, _depC, _depD, _depE);
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
        final E e = _depE.getValue();
        _value = _func.apply(a, b, c, d, e);
        _func = null;
        _depA = null;
        _depB = null;
        _depC = null;
        _depD = null;
        _depE = null;
        return _value;
    }

    @Override
    public String toStringUnforced(final String name) {
        return "(_5_) -> " + name;
    }
}
