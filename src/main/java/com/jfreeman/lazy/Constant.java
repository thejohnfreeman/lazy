package com.jfreeman.lazy;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * A lazy constant has no dependencies.
 *
 * @author jfreeman
 */
public class Constant<T>
    implements LazyValue<T>
{
    private final T _value;

    private Constant(T value) {
        _value = value;
    }

    public static <T> Constant<T> create(T value) {
        return new Constant<>(value);
    }

    @Override
    public List<LazyValue<?>> getDependencies() {
        return ImmutableList.of();
    }

    @Override
    public T force() throws IllegalStateException {
        return _value;
    }

    @Override
    public boolean isForced() {
        return true;
    }
}
