package com.jfreeman.attribute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jfreeman.lazy.LateBound;
import com.jfreeman.lazy.Lazy;
import com.jfreeman.lazy.Output;

/**
 * An attribute that may be "read" (in the sense that its lazy wrapper is
 * stored) before it is written.
 *
 * @author jfreeman
 */
public class LateBoundAttribute<N, T>
    implements Attribute<N, T>
{
    private final Map<N, Lazy<T>> _attrs = new ConcurrentHashMap<>();

    private Lazy<T> getOrCreate(N node) {
        return _attrs.computeIfAbsent(node, k -> LateBound.of());
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * May be called before the node's value has been
     * {@link #put(Object, Lazy)}.
     */
    @Override
    public Lazy<T> get(N node) {
        return getOrCreate(node);
    }

    @Override
    public void put(N node, Lazy<T> value) {
        final Lazy<T> existing = _attrs.get(node);
        if (existing != null) {
            if (existing instanceof LateBound) {
                ((LateBound<T>)existing).bind(value);
            } else {
                throw new IllegalArgumentException(String.format(
                        "attribute already assigned:\n  key: %s\n  value: %s",
                        node, value));
            }
        }
        _attrs.put(node, value);
    }

    /**
     * Return an output wrapper for a multi-output method.
     */
    public Output<T> put(N node) {
        final Output<T> output = new Output<>();
        put(node, output);
        return output;
    }
}
