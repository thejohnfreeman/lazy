package com.jfreeman.attribute;

import com.jfreeman.lazy.Lazy;

public interface Attribute<N, T>
{
    Lazy<T> get(N node);

    void put(N node, Lazy<T> value);
}
