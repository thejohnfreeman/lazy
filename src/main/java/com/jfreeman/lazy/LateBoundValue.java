package com.jfreeman.lazy;

import java.util.List;

import com.sun.istack.internal.NotNull;

/**
 * A value whose provenance is unknown at construction.
 *
 * @param <T> the type of the value.
 * @author jfreeman
 */
public class LateBoundValue<T>
    implements LazyValue<T>
{
    private LazyValue<T> _value = null;

    private LateBoundValue() {}

    public static <T> LateBoundValue<T> create() {
        return new LateBoundValue<>();
    }

    /**
     * Bind the value. May only be called once.
     * @param value the value this object should represent.
     */
    public void bind(@NotNull LazyValue<T> value) {
        if (_value != null) {
            throw new IllegalStateException("already bound");
        }
        _value = value;
    }

    public boolean isBound() {
        return _value != null;
    }

    private void assertBound() {
        if (!isBound()) {
            throw new IllegalStateException("unbound");
        }
    }

    @Override
    public List<LazyValue<?>> getDependencies() {
        assertBound();
        return _value.getDependencies();
    }

    @Override
    public T force() throws IllegalStateException {
        assertBound();
        return _value.force();
    }

    @Override
    public boolean isForced() {
        return isBound() && _value.isForced();
    }
}
