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
public class TriThunk<T, A, B, C>
    implements LazyValue<T>
{
    private T _value = null;
    private boolean _forced = false;
    private final LazyValue<A> _depA;
    private final LazyValue<B> _depB;
    private final LazyValue<C> _depC;
    private final TriFunction<A, B, C, T> _func;

    private TriThunk(LazyValue<A> a, LazyValue<B> b, LazyValue<C> c,
        TriFunction<A, B, C, T> func)
    {
        _depA = a;
        _depB = b;
        _depC = c;
        _func = func;
    }

    public static <T, A, B, C> TriThunk<T, A, B, C> create(
        LazyValue<A> a, LazyValue<B> b, LazyValue<C> c,
        TriFunction<A, B, C, T> func)
    {
        return new TriThunk<>(a, b, c, func);
    }

    @Override
    public List<LazyValue<?>> getDependencies() {
        return ImmutableList.of(_depA, _depB, _depC);
    }

    @Override
    public T force() throws IllegalStateException {
        if (!_forced) {
            A a = _depA.force();
            B b = _depB.force();
            C c = _depC.force();
            _value = _func.apply(a, b, c);
            _forced = true;
        }
        return _value;
    }

    @Override
    public boolean isForced() {
        return _forced;
    }
}
