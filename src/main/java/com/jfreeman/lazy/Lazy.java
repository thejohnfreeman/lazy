package com.jfreeman.lazy;

/**
 * A lazy value with dependencies.
 * <p>
 * A lazy value is stateful. Before it is forced, it can return its
 * dependencies, but not its value. After it is forced, it can return its value,
 * but might not be able to return its (former) dependencies.
 * <p>
 * A lazy value cannot be
 * forced until all of its dependencies are forced. An iterative algorithm
 * for forcing a directed acyclic graph of lazy values is provided in
 * {@link LazyHelp#force(Lazy)}. It is the recommended interface for forcing
 * evaluation.
 * <p>
 * Note: In generic implementing types from this package, the value type is
 * always the first type parameter, unlike functional interfaces, where the
 * result type is always the last type parameter.
 */
public interface Lazy<T> {
    /**
     * Returns whether this value has been forced yet.
     *
     * @return {@code true} iff this value has been successfully forced
     */
    boolean isForced();

    /**
     * Returns an immutable list of this value's dependencies.
     *
     * @return an immutable list of this value's dependencies
     * @throws IllegalStateException if {@code isForced()}
     */
    Iterable<? extends Lazy<?>> getDependencies() throws IllegalStateException;

    /**
     * Evaluates and returns this value. May be called repeatedly until it
     * succeeds, but not once after.
     *
     * @return the value
     * @throws IllegalStateException if any dependencies are unforced,
     * or if this value was already successfully forced
     * @see LazyHelp#force(Lazy)
     */
    T force() throws IllegalStateException;

    /**
     * Returns this value. May not be called until after evaluation is forced.
     *
     * @return the value
     * @throws IllegalStateException if {@code !isForced()}
     * @see LazyHelp#force(Lazy)
     */
    T getValue() throws IllegalStateException;
}
