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
class Thunk<T, A>
    implements Lazy<T>
{

    private T _value = null;
    private boolean _forced = false;
    private final Lazy<A> _dep;
    private final Function<A, T> _func;

    private Thunk(Lazy<A> dep, Function<A, T> func) {
        _dep = dep;
        _func = func;
    }

    public static <T, A> Thunk<T, A> create(
        Lazy<A> dep, Function<A, T> func)
    {
        return new Thunk<>(dep, func);
    }

    @Override
    public List<Lazy<?>> getDependencies() {
        return ImmutableList.<Lazy<?>>of(_dep);
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
