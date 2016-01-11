package com.jfreeman.lazy;

import java.util.List;

/**
 * A lazy value with dependencies.
 * <p/>
 * A lazy value has no value until it is forced. A lazy value cannot be
 * forced until all of its dependencies are forced. An iterative algorithm
 * for forcing a directed acyclic graph of lazy values is provided in
 * {@link LazyHelp#force(Lazy)}. It is the recommended interface for forcing
 * evaluation.
 * <p/>
 * Note: In generic implementing types from this package, the value type is
 * always the first type parameter, unlike functional interfaces, where the
 * result type is always the last type parameter.
 */
public interface Lazy<T> {
    /**
     * @return an immutable list of dependencies, or {@code null} if they are
     * not known.
     */
    List<Lazy<?>> getDependencies();

    /**
     * Returns this value. May not be called until after evaluation is forced.
     * @return the value.
     * @throws IllegalStateException if {@code !isForced()}
     * @see LazyHelp#force(Lazy)
     */
    T getValue() throws IllegalStateException;

    /**
     * Evaluate this value and return it. May be called repeatedly until it
     * succeeds, but not once after.
     * @return the value.
     * @throws IllegalStateException if any dependencies are unforced,
     * or if this value was already successfully forced.
     * @see LazyHelp#force(Lazy)
     */
    T force() throws IllegalStateException;

    /**
     * Return whether this value has been forced yet.
     * @return {@code true} iff this value has been successfully forced.
     */
    boolean isForced();
}
