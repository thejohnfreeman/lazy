package com.jfreeman.lazy;

import java.util.List;

/**
 * Abstract base class for thunks that return a single value.
 *
 * @param <T> the type of the value
 */
public abstract class AbstractThunk<T>
    implements Lazy<T>, Taggable<TaggedThunk<T>>
{
    /**
     * A null _value does not mean it has not been computed. For that, call
     * {@link #isForced()}.
     */
    protected T _value = null;

    @Override
    public TaggedThunk<T> tag(List<String> tags) {
        return TaggedThunk.of(this).tag(tags);
    }

    @Override
    public T getValue()
        throws IllegalStateException
    {
        if (!isForced()) {
            throw new IllegalStateException("not yet forced");
        }
        return _value;
    }
}
