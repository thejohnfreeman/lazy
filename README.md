A library for type-safe, tractable lazy evaluation and late binding in Java.

[![Build Status](https://travis-ci.org/thejohnfreeman/lazy.svg?branch=master)](https://travis-ci.org/thejohnfreeman/lazy)
[![Javadocs](http://javadoc.io/badge/com.thejohnfreeman/lazy.svg)](http://javadoc.io/doc/com.thejohnfreeman/lazy)

```xml
<dependency>
    <groupId>com.thejohnfreeman</groupId>
    <artifactId>lazy</artifactId>
    <version>0.2.0</version>
</dependency>
```

Compare:

- [totallylazy](https://github.com/bodar/totallylazy)

  Actively developed, but almost no documentation at the moment. Might be
  worth contributing to that instead of this.

- [java.util.concurrent.CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)

  Does not seem to handle multiple inputs, much less multiple outputs.
