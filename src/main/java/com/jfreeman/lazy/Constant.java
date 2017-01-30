package com.jfreeman.lazy;

/**
 * A lazy constant (i.e. a value with no dependencies).
 *
 * @author jfreeman
 */
public final class Constant<T>
    implements Lazy<T>
{
    private final T _value;

    private Constant(T value) {
        _value = value;
    }

    public static <T> Constant<T> of(T value) {
        return new Constant<>(value);
    }

    @Override
    public boolean isForced() {
        return true;
    }

    @Override
    public Iterable<? extends Lazy<?>> getDependencies()
        throws IllegalStateException
    {
        throw new IllegalStateException("already forced");
    }

    @Override
    public T force()
        throws IllegalStateException
    {
        throw new IllegalStateException("already forced");
    }

    @Override
    public T getValue()
        throws IllegalStateException
    {
        return _value;
    }
}
