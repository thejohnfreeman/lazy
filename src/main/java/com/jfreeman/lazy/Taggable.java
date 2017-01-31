package com.jfreeman.lazy;

import java.util.Arrays;
import java.util.List;

/**
 * For classes that can be tagged (with strings) for debugging.
 *
 * <p>This interface is more worried about convenience than performance since
 * its purpose is exclusively for debugging.</p>
 */
public interface Taggable<T extends Taggable> {
    T tag(final List<String> tags);

    default T tag(final String... tags) {
        return tag(Arrays.asList(tags));
    }

    /**
     * Tag with the location of the call to this method.
     *
     * <p>The effectiveness of this method depends on the JVM implementation.</p>
     */
    default T debug() {
        final StackTraceElement frame = Thread.currentThread().getStackTrace()[2];
        final String tag = String.format("%s.%s() @ %s:%s",
                frame.getClassName(), frame.getMethodName(),
                frame.getFileName(), frame.getLineNumber());
        return tag(tag);
    }
}
