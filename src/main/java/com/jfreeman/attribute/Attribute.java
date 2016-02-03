package com.jfreeman.attribute;

import com.jfreeman.lazy.Lazy;

/**
 * @author jfreeman
 */
public interface Attribute<N, T>
{
    Lazy<T> get(N node);
    void set(N node, Lazy<T> value);
}
