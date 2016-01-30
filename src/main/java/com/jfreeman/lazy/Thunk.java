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

    private final Lazy<A> _dep;
    private Function<A, T> _func;
    private T _value = null;

    private Thunk(Lazy<A> dep, Function<A, T> func) {
        _dep = dep;
        _func = func;
    }

    public static <T, A> Thunk<T, A> create(Lazy<A> dep, Function<A, T> func)
    {
        return new Thunk<>(dep, func);
    }

    @Override
    public List<Lazy<?>> getDependencies() {
        return ImmutableList.<Lazy<?>>of(_dep);
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
        if (_func == null) {
            throw new IllegalStateException("already forced");
        }
        _value = _func.apply(_dep.getValue());
        _func = null;
        return _value;
    }

    @Override
    public boolean isForced() {
        return _func == null;
    }
}
