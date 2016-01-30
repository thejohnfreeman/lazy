package com.jfreeman.lazy;

import java.util.List;

/**
 * A value whose provenance is unknown at construction.
 *
 * @param <T> the type of the value.
 * @author jfreeman
 */
public class LateBound<T>
    implements Lazy<T>
{
    private Lazy<T> _value = null;

    private LateBound() {}

    public static <T> LateBound<T> create() {
        return new LateBound<>();
    }

    /**
     * Bind the value. May only be called once.
     * @param value the value this object should represent.
     */
    public void bind(Lazy<T> value) {
        if (_value != null) {
            throw new IllegalStateException("already bound");
        }
        _value = value;
    }

    public boolean isBound() {
        return _value != null;
    }

    @Override
    public List<Lazy<?>> getDependencies() {
        return isBound() ? _value.getDependencies() : null;
    }

    @Override
    public T getValue() throws IllegalStateException {
        return _value.getValue();
    }

    @Override
    public T force() throws IllegalStateException {
        if (!isBound()) {
            throw new IllegalStateException("unbound");
        }
        return _value.force();
    }

    @Override
    public boolean isForced() {
        return isBound() && _value.isForced();
    }
}
