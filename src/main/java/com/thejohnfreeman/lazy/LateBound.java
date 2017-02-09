package com.thejohnfreeman.lazy;

/**
 * A value whose provenance is unknown at construction.
 *
 * @param <T> the type of the value
 * @author thejohnfreeman
 */
public final class LateBound<T>
    implements TaggableLazy<T>
{
    private Lazy<? extends T> _value;

    private LateBound(final Lazy<T> value) {
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
    public static <T> LateBound<T> of(final Lazy<T> value) {
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
    public LateBound<T> bind(final Lazy<? extends T> value)
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
    public Iterable<? extends Lazy<?>> getDependencies()
        throws IllegalStateException
    {
        if (!isBound()) {
            throw new IllegalStateException("unbound");
        }
        return _value.getDependencies();
    }

    @Override
    public T forceThis()
        throws IllegalStateException
    {
        if (!isBound()) {
            throw new IllegalStateException("unbound");
        }
        return _value.forceThis();
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

    @Override
    public String toStringUnforced(final String name) {
        return "(???) -> " + name;
    }

    @Override
    public String toString() {
        return isBound() ? _value.toString() : toStringUnforced("???");
    }
}
