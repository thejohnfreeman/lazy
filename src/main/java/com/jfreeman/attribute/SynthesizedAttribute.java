package com.jfreeman.attribute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jfreeman.lazy.Lazy;

/**
 * @author jfreeman
 */
public class SynthesizedAttribute<N, T>
    implements Attribute<N, T>
{
    private final Map<N, Lazy<T>> _attrs = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     *
     * May not be called for a node until after its value has been
     * {@link #set(Object, Lazy)}.
     */
    @Override
    public Lazy<T> get(N node) {
        return _attrs.get(node);
    }

    /**
     * {@inheritDoc}
     *
     * Must be called before {@link #get(Object)} for the same node.
     */
    @Override
    public void set(N node, Lazy<T> value) {
        _attrs.put(node, value);
    }
}
