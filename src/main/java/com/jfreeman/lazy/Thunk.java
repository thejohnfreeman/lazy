package com.jfreeman.lazy;

import java.util.function.Function;

import com.google.common.collect.ImmutableList;

/**
 * A lazy value computed from a single dependency.
 *
 * @param <T> the type of the value
 * @param <A> the type of the dependency
 */
public final class Thunk<T, A>
    extends AbstractThunk<T>
{
    private Lazy<A> _dep;
    /**
     * _func is assigned at construction. While it is non-null, _value has not
     * yet been computed. When forced, the function will be called once,
     * _value will be assigned to its result, and _func will be assigned to
     * null (both to indicate evaluation has occurred, and to allow the
     * function to be garbage-collected).
     */
    private Function<A, T> _func;

    private Thunk(Lazy<A> dep, Function<A, T> func) {
        _dep = dep;
        _func = func;
    }

    public static <T, A> Thunk<T, A> of(Lazy<A> dep, Function<A, T> func) {
        return new Thunk<>(dep, func);
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
        return ImmutableList.of(_dep);
    }

    @Override
    public T force()
        throws IllegalStateException
    {
        if (isForced()) {
            throw new IllegalStateException("already forced");
        }
        _value = _func.apply(_dep.getValue());
        _func = null;
        _dep = null;
        return _value;
    }
}
