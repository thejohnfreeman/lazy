package com.jfreeman.lazy;

public interface TaggableLazy<T>
    extends Lazy<T>, Taggable<TaggedLazy<T>>
{
    @Override
    default TaggedLazy<T> tag(final String tag, final String origin) {
        return TaggedLazy.of(this, tag, origin);
    }
}
