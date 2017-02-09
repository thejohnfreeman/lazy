package com.thejohnfreeman.lazy;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * An iterative algorithm for forcing a directed acyclic graph of lazy values.
 */
public final class Force
{
    /** Just a namespace, so no constructor. */
    private Force() {}

    /**
     * When forced, a long chain of lazy values can cause a stack overflow.
     * This method forces a value iteratively.
     *
     * @param <T> the type of the value
     * @param value the lazy value
     * @return the forced value
     * @throws IllegalStateException
     *     if any value in the dependency graph cannot be evaluated
     */
    public static <T> T force(final Lazy<T> value)
        throws IllegalStateException
    {
        final ArrayDeque<Context> values = new ArrayDeque<>();
        values.push(new Context(value));

        while (!values.isEmpty()) {
            forceTop(values);
        }

        return value.getValue();
    }

    /**
     * If the top value is ready, force it; otherwise, push one of its unforced
     * dependencies. This is a separate function to make use of `return` as a
     * multi-level `break`. This particular case does not fit the labeled break
     * pattern for Java.
     */
    private static void forceTop(final ArrayDeque<Context> values) {
        final Context context = values.getFirst();
        if (!context.value.isForced()) {
            for (final Lazy<?> dependency : context) {
                if (!dependency.isForced()) {
                    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
                    final Context ctx = new Context(dependency);
                    values.push(ctx);
                    return;
                }
            }
            context.value.forceThis();
        }
        values.pop();
    }

    /**
     * A context is a lazy value and an iterator through its dependencies. Once
     * all of the dependencies have been forced, the value is forced.
     */
    private static class Context
        implements Iterable<Lazy<?>>, Iterator<Lazy<?>>
    {
        public final Lazy<?> value;
        private Iterator<? extends Lazy<?>> _iterator;

        public Context(final Lazy<?> value) {
            this.value = value;
        }

        @Override
        public Iterator<Lazy<?>> iterator() {
            if (_iterator == null) {
                _iterator = value.getDependencies().iterator();
            }
            return this;
        }

        @Override
        public boolean hasNext() {
            return _iterator.hasNext();
        }

        @Override
        public Lazy<?> next() {
            return _iterator.next();
        }
    }
}
