package com.jfreeman.lazy;

import java.util.List;
import java.util.Objects;

/**
 */
public class Output<T>
    implements Lazy<T>
{
    private Method _method = null;
    private T _value = null;

    /**
     * Set the method that writes to this output. Outputs are generally
     * constructed before their method (so it knows what types to pass to its
     * function).
     */
    public void setMethod(Method method) {
        _method = method;
    }

    /**
     * Assign the value from within a multi-output method.
     */
    public void assign(T value) {
        Objects.requireNonNull(value);
        if (_value != null) {
            throw new UnsupportedOperationException(String.format(
                    "output already assigned:\n  old: %s\n  new: %s",
                    _value, value));
        }
        _value = value;
    }

    /**
     * Assert that the output was assigned and disconnect it from its method.
     */
    public void seal() {
        if (_value == null) {
            throw new IllegalStateException("output never assigned");
        }
        _method = null;
    }

    @Override
    public boolean isForced() {
        return _method == null;
    }

    @Override
    public List<Lazy<?>> getDependencies()
        throws IllegalStateException
    {
        return _method.getInputs();
    }

    @Override
    public T force()
        throws IllegalStateException
    {
        _method.force();
        return _value;
    }

    @Override
    public T getValue()
        throws IllegalStateException
    {
        return _value;
    }
}
