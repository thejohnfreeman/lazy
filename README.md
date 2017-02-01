A library for type-safe, tractable lazy evaluation and late binding in Java.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.thejohnfreeman/lazy/badge.svg)](http://search.maven.org/#search|gav|1|g%3A%22com.thejohnfreeman%22%20AND%20a%3A%22lazy%22)
[![Javadocs](http://javadoc.io/badge/com.thejohnfreeman/lazy.svg)](http://javadoc.io/doc/com.thejohnfreeman/lazy)
[![Build Status](https://travis-ci.org/thejohnfreeman/lazy.svg?branch=master)](https://travis-ci.org/thejohnfreeman/lazy)

```xml
<dependency>
    <groupId>com.thejohnfreeman</groupId>
    <artifactId>lazy</artifactId>
    <version>0.3.0</version>
</dependency>
```

Compare:

- [totallylazy](https://github.com/bodar/totallylazy)

  Actively developed, but almost no documentation at the moment. Might be
  worth contributing to that instead of this.

- [java.util.concurrent.CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)

  Does not seem to handle multiple inputs, much less multiple outputs.
