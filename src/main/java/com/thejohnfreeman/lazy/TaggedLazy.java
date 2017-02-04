package com.thejohnfreeman.lazy;

/**
 * Decorates a lazy value with information for debugging.
 *
 * @param <T> the type of the value
 */
public final class TaggedLazy<T>
    implements Lazy<T>
{
    private final Lazy<T> _value;
    private final String _tag;
    private final String _origin;

    private TaggedLazy(
        final Lazy<T> value, final String tag, final String origin)
    {
        this._value = value;
        this._tag = tag;
        this._origin = origin;
    }

    public static <T> TaggedLazy<T> of(
        final Lazy<T> value, final String tag, final String origin)
    {
        return new TaggedLazy<>(value, tag, origin);
    }

    @Override
    public String toString() {
        if (_value.isForced()) {
            return _value.toString();
        }
        return _value.toStringUnforced(_tag) + " at " + _origin;
    }

    @Override
    public boolean isForced() {
        return _value.isForced();
    }

    @Override
    public Iterable<? extends Lazy<?>> getDependencies()
        throws IllegalStateException
    {
        return _value.getDependencies();
    }

    @Override
    public T force()
        throws IllegalStateException
    {
        return _value.force();
    }

    @Override
    public T getValue()
        throws IllegalStateException
    {
        return _value.getValue();
    }
}
