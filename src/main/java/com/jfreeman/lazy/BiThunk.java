package com.jfreeman.lazy;

import java.util.List;

import com.google.common.collect.ImmutableList;

import com.jfreeman.function.BiFunction;

/**
 * A lazy value with two dependencies.
 *
 * @param <T> the type of the value.
 * @param <A> the type of the first dependency.
 * @param <B> the type of the second dependency.
 * @author jfreeman
 */
public class BiThunk<T, A, B>
    implements LazyValue<T>
{
    private T _value = null;
    private boolean _forced = false;
    private final LazyValue<A> _depA;
    private final LazyValue<B> _depB;
    private final BiFunction<A, B, T> _func;

    private BiThunk(LazyValue<A> a, LazyValue<B> b, BiFunction<A, B, T> func) {
        _depA = a;
        _depB = b;
        _func = func;
    }

    public static <T, A, B> BiThunk<T, A, B> create(
        LazyValue<A> a, LazyValue<B> b, BiFunction<A, B, T> func)
    {
        return new BiThunk<>(a, b, func);
    }

    @Override
    public List<LazyValue<?>> getDependencies() {
        return ImmutableList.of(_depA, _depB);
    }

    @Override
    public T force() throws IllegalStateException {
        if (!_forced) {
            A a = _depA.force();
            B b = _depB.force();
            _value = _func.apply(a, b);
            _forced = true;
        }
        return _value;
    }

    @Override
    public boolean isForced() {
        return _forced;
    }
}
