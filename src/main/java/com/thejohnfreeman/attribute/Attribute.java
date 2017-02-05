package com.thejohnfreeman.attribute;

import com.thejohnfreeman.lazy.Lazy;

public interface Attribute<N, T>
{
    Lazy<T> get(N node);

    void put(N node, Lazy<T> value) throws IllegalStateException;
}
