package com.jfreeman.lazy;

import java.util.List;

/**
 * A value whose provenance is unknown at construction.
 *
 * @param <T> the type of the value
 * @author jfreeman
 */
public class LateBound<T>
    implements Lazy<T>
{
    private Lazy<T> _value;

    private LateBound(Lazy<T> value) {
        _value = value;
    }

    /**
     * Constructs and returns an unbound value.
     *
     * @param <T> the type of the value
     * @return an unbound value
     */
    public static <T> LateBound<T> of() {
        return new LateBound<>(null);
    }

    /**
     * Constructs and returns a bound value.
     *
     * @param value a lazy value
     * @param <T> the type of the value
     * @return a bound value
     */
    public static <T> LateBound<T> of(Lazy<T> value) {
        return new LateBound<>(value);
    }

    public boolean isBound() {
        return _value != null;
    }

    /**
     * Binds the value. May only be called once.
     *
     * @param value a lazy value
     * @return this object, for chaining
     * @throws IllegalStateException if this value is already bound
     */
    public LateBound<T> bind(Lazy<T> value)
        throws IllegalStateException
    {
        if (isBound()) {
            throw new IllegalStateException("already bound");
        }
        _value = value;
        return this;
    }

    @Override
    public boolean isForced() {
        return isBound() && _value.isForced();
    }

    @Override
    public List<Lazy<?>> getDependencies()
        throws IllegalStateException
    {
        if (!isBound()) {
            throw new IllegalStateException("unbound");
        }
        return _value.getDependencies();
    }

    @Override
    public T force()
        throws IllegalStateException
    {
        if (!isBound()) {
            throw new IllegalStateException("unbound");
        }
        return _value.force();
    }

    @Override
    public T getValue()
        throws IllegalStateException
    {
        if (!isBound()) {
            throw new IllegalStateException("unbound");
        }
        return _value.getValue();
    }
}
