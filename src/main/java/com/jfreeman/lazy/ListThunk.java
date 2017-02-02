package com.jfreeman.lazy;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A lazy value that depends on a list of values of the same type.
 *
 * @param <T> the type of the value
 * @param <E> the element type of the list
 */
public class ListThunk<T, E>
    extends AbstractThunk<T>
{
    private List<Lazy<E>> _deps;
    private Function<List<E>, T> _func;

    private ListThunk(
        List<Lazy<E>> dependencies, Function<List<E>, T> function)
    {
        this._deps = dependencies;
        this._func = function;
    }

    public static <T, E> ListThunk<T, E> of(
        List<Lazy<E>> dependencies, Function<List<E>, T> function)
    {
        return new ListThunk<>(dependencies, function);
    }

    /**
     * Essentially {@code sequenceA}: turn a {@code List} of {@code Lazy}s
     * into a {@code Lazy} of a {@code List}.
     *
     * @param dependencies a list of lazy values
     * @param <E> the type of the values
     * @return a lazy list of values
     */
    public static <E> ListThunk<List<E>, E> sequence(
        List<Lazy<E>> dependencies)
    {
        return ListThunk.of(dependencies, x -> x);
    }

    @Override
    public boolean isForced() {
        return _func == null;
    }

    @Override
    public Iterable<? extends Lazy<?>> getDependencies()
        throws IllegalStateException
    {
        if (isForced()) {
            throw new IllegalStateException("already forced");
        }
        return _deps;
    }

    @Override
    public T force()
        throws IllegalStateException
    {
        if (isForced()) {
            throw new IllegalStateException("already forced");
        }
        final List<E> args =
            _deps.stream().map(Lazy::getValue).collect(Collectors.toList());
        _value = _func.apply(args);
        _func = null;
        _deps = null;
        return _value;
    }

    @Override
    public String toStringUnforced(final String name) {
        return String.format("(List[%d]) -> %s", _deps.size(), name);
    }
}
