package com.jfreeman.attribute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jfreeman.lazy.LateBound;
import com.jfreeman.lazy.Lazy;

/**
 * @author jfreeman
 */
public class InheritedAttribute<N, T>
    implements Attribute<N, T>
{
    private final Map<N, LateBound<T>> _attrs = new ConcurrentHashMap<>();

    private LateBound<T> getOrCreate(N node) {
        LateBound<T> attr = _attrs.get(node);
        if (attr == null) {
            attr = LateBound.<T>create();
            _attrs.put(node, attr);
        }
        return attr;
    }

    /**
     * {@inheritDoc}
     *
     * May be called before the node's value has been
     * {@link #set(Object, Lazy)}.
     */
    @Override
    public Lazy<T> get(N node) {
        return getOrCreate(node);
    }

    @Override
    public void set(N node, Lazy<T> value) {
        getOrCreate(node).bind(value);
    }
}
