package com.thejohnfreeman.lazy;

/**
 * For classes that can be tagged (with a string and origin) for debugging.
 *
 * <p>
 * This interface is more worried about convenience than performance since
 * its purpose is exclusively for debugging.
 */
public interface Taggable<T>
{
    T tag(final String tag, final String origin);

    /**
     * Tag with the call to this method as the origin.
     *
     * <p>
     * The effectiveness of this method depends on the JVM implementation.
     */
    default T tag(final String tag) {
        return tag(tag, /* stackLevel: */1);
    }

    /**
     * Tag with a frame above the call to this method as the origin.
     *
     * @param tag the tag
     * @param stackLevel number of stack frames above the call to this method
     *     to use for the origin
     * @return a tagged object
     */
    default T tag(final String tag, final int stackLevel) {
        // Level 0 is the Throwable constructor. Level 1 is the call to the
        // Throwable constructor within getStackTrace(). Level 2 is this call
        // to getStackTrace(). Add 2 to whatever our caller passed us so they
        // can think relative to their frame of reference.
        final int realStackLevel = stackLevel + 2;
        final StackTraceElement frame = Thread
            .currentThread().getStackTrace()[realStackLevel];
        final String fqcn = frame.getClassName();
        final String uqcn = fqcn.substring(fqcn.lastIndexOf('.') + 1);
        final String origin = String.format("%s.%s() @ %s:%s",
            uqcn, frame.getMethodName(),
            frame.getFileName(), frame.getLineNumber());
        return tag(tag, origin);
    }
}
