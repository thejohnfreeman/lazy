package com.thejohnfreeman.lazy;

import com.google.common.collect.ImmutableList;

import com.thejohnfreeman.function.Function4;

/**
 * A lazy value computed from four dependencies.
 *
 * @param <T> the type of the value
 * @param <A> the type of the first dependency
 * @param <B> the type of the second dependency
 * @param <C> the type of the third dependency
 * @param <D> the type of the fourth dependency
 */
public final class Thunk4<T, A, B, C, D>
    extends AbstractThunk<T>
{
    private Lazy<A> _depA;
    private Lazy<B> _depB;
    private Lazy<C> _depC;
    private Lazy<D> _depD;
    private Function4<A, B, C, D, T> _func;

    private Thunk4(
        final Lazy<A> a, final Lazy<B> b, final Lazy<C> c, final Lazy<D> d,
        final Function4<A, B, C, D, T> func)
    {
        _depA = a;
        _depB = b;
        _depC = c;
        _depD = d;
        _func = func;
    }

    public static <T, A, B, C, D> Thunk4<T, A, B, C, D> of(
        final Lazy<A> a, final Lazy<B> b, final Lazy<C> c, final Lazy<D> d,
        final Function4<A, B, C, D, T> func)
    {
        return new Thunk4<>(a, b, c, d, func);
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
        return ImmutableList.of(_depA, _depB, _depC, _depD);
    }

    @Override
    public T forceThis()
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
    public String toStringUnforced(final String name) {
        return "(_4_) -> " + name;
    }
}
