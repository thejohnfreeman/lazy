package com.jfreeman.lazy;

import java.util.List;

import com.google.common.collect.ImmutableList;

import com.jfreeman.function.Function;

/**
 * A lazy value with a single dependency.
 *
 * @param <T> the type of the value.
 * @param <A> the type of the dependency.
 * @author jfreeman
 */
public class Thunk<T, A>
    implements LazyValue<T>
{

    private T _value = null;
    private boolean _forced = false;
    private final LazyValue<A> _dep;
    private final Function<A, T> _func;

    private Thunk(LazyValue<A> dep, Function<A, T> func) {
        _dep = dep;
        _func = func;
    }

    public static <T, A> Thunk<T, A> create(
        LazyValue<A> dep, Function<A, T> func)
    {
        return new Thunk<>(dep, func);
    }

    @Override
    public List<LazyValue<?>> getDependencies() {
        return ImmutableList.<LazyValue<?>>of(_dep);
    }

    @Override
    public T force() throws IllegalStateException {
        if (!_forced) {
            final A dep = _dep.force();
            _value = _func.apply(dep);
            _forced = true;
        }
        return _value;
    }

    @Override
    public boolean isForced() {
        return _forced;
    }
}
