package com.jfreeman.lazy;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

/**
 * Decorates a thunk with string tags for debugging.
 *
 * @param <T> the type of the value
 */
public final class TaggedThunk<T>
    implements Lazy<T>, Taggable<TaggedThunk<T>>
{
    private final Lazy<T> _thunk;
    private final List<String> _tags = new ArrayList<>();

    private TaggedThunk(final Lazy<T> thunk) {
        this._thunk = thunk;
    }

    public static <T> TaggedThunk<T> of(final Lazy<T> thunk) {
        return new TaggedThunk<>(thunk);
    }

    @Override
    public TaggedThunk<T> tag(final List<String> tag) {
        _tags.addAll(tag);
        return this;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s",
            Joiner.on(", ").join(_tags), _thunk.toString());
    }

    @Override
    public boolean isForced() {
        return _thunk.isForced();
    }

    @Override
    public Iterable<? extends Lazy<?>> getDependencies()
        throws IllegalStateException
    {
        return _thunk.getDependencies();
    }

    @Override
    public T force()
        throws IllegalStateException
    {
        return _thunk.force();
    }

    @Override
    public T getValue()
        throws IllegalStateException
    {
        return _thunk.getValue();
    }
}
