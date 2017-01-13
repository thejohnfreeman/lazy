package com.jfreeman.lazy;

import java.util.List;

/**
 * A function with an arbitrary number of inputs and outputs.
 */
public interface Method {
    List<Lazy<?>> getInputs();

    List<Lazy<?>> getOutputs();

    void force() throws IllegalStateException;
}
