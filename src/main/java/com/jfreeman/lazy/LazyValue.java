package com.jfreeman.lazy;

import java.util.List;

/**
 * A lazy value with dependencies.
 * <p/>
 * A lazy value has no value until it is forced. A lazy value cannot be
 * forced until all of its dependencies are forced. Forcing a lazy value will
 * recursively force its dependencies, but an evaluation graph that is too
 * deep can overflow the call stack, so machinery is provided in {@link
 * LazyValues} for safely forcing a lazy value.
 * <p/>
 * In generic implementing types from this package, the value type is always the
 * first type parameter.
 */
interface LazyValue<T> {
    List<LazyValue<?>> getDependencies();
    T force() throws IllegalStateException;
    boolean isForced();
}
