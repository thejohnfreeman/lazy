package com.jfreeman.lazy;

import java.util.List;

/**
 * A lazy value with dependencies.
 * <p/>
 * A lazy value has no value until it is forced. A lazy value cannot be
 * forced until all of its dependencies are forced. Forcing a lazy value will
 * recursively force its dependencies, but an evaluation graph that is too
 * deep can overflow the call stack, so machinery is provided in {@link
 * LazyHelp} for safely forcing a lazy value.
 * <p/>
 * In generic implementing types from this package, the value type is always the
 * first type parameter.
 */
public interface Lazy<T> {
    /**
     * @return an immutable list of dependencies, or {@code null} if they are
     * not known.
     */
    List<Lazy<?>> getDependencies();

    /**
     * Evaluate this value and return it.
     * @return the value.
     * @throws IllegalStateException if unprepared for evaluation.
     */
    T force() throws IllegalStateException;

    /**
     * Return whether this value has been forced yet.
     * @return {@code true} iff this value has been forced.
     */
    boolean isForced();
}
