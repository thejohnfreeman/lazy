package com.thejohnfreeman.attribute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.thejohnfreeman.lazy.Lazy;

/**
 * An attribute that is always written before it is read.
 *
 * @author thejohnfreeman
 */
public class EarlyBoundAttribute<N, T>
    implements Attribute<N, T>
{
    private final Map<N, Lazy<T>> _attrs = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     *
     * <p>
     * May not be called for a node until after its value has been
     * {@link #put(Object, Lazy)}.
     */
    @Override
    public Lazy<T> get(N node) {
        return _attrs.get(node);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Must be called before {@link #get(Object)} for the same node.
     */
    @Override
    public void put(N node, Lazy<T> value) {
        _attrs.put(node, value);
    }
}
