package com.thejohnfreeman.lazy;

import com.google.common.collect.ImmutableList;

import com.thejohnfreeman.function.Function6;

/**
 * A lazy value computed from six dependencies.
 *
 * @param <T> the type of the value
 * @param <A> the type of the first dependency
 * @param <B> the type of the second dependency
 * @param <C> the type of the third dependency
 * @param <D> the type of the fourth dependency
 * @param <E> the type of the fifth dependency
 * @param <F> the type of the sixth dependency
 */
public final class Thunk6<T, A, B, C, D, E, F>
    extends AbstractThunk<T>
{
    private Lazy<A> _depA;
    private Lazy<B> _depB;
    private Lazy<C> _depC;
    private Lazy<D> _depD;
    private Lazy<E> _depE;
    private Lazy<F> _depF;
    private Function6<A, B, C, D, E, F, T> _func;

    private Thunk6(
        final Lazy<A> a, final Lazy<B> b, final Lazy<C> c, final Lazy<D> d,
        final Lazy<E> e, final Lazy<F> f,
        final Function6<A, B, C, D, E, F, T> func)
    {
        _depA = a;
        _depB = b;
        _depC = c;
        _depD = d;
        _depE = e;
        _depF = f;
        _func = func;
    }

    public static <T, A, B, C, D, E, F> Thunk6<T, A, B, C, D, E, F> of(
        final Lazy<A> a, final Lazy<B> b, final Lazy<C> c, final Lazy<D> d,
        final Lazy<E> e, final Lazy<F> f,
        final Function6<A, B, C, D, E, F, T> func)
    {
        return new Thunk6<>(a, b, c, d, e, f, func);
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
        return ImmutableList.of(_depA, _depB, _depC, _depD, _depE, _depF);
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
        final F f = _depF.getValue();
        _value = _func.apply(a, b, c, d, e, f);
        _func = null;
        _depA = null;
        _depB = null;
        _depC = null;
        _depD = null;
        _depE = null;
        _depF = null;
        return _value;
    }

    @Override
    public String toStringUnforced(final String name) {
        return "(_6_) -> " + name;
    }
}
