# lazy
------

A library for type-safe, tractable lazy evaluation and late binding in Java.
Useful for defining [syntax-directed translations][sdt] with
[visitors][visitor] that look nearly identical to the semantic rules of their
[syntax-directed definitions][sdd].

[sdt]: https://en.wikipedia.org/wiki/Syntax-directed_translation
[visitor]: https://en.wikipedia.org/wiki/Visitor_pattern
[sdd]: http://www.csd.uwo.ca/~moreno//CS447/Lectures/Translation.html/node1.html

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.thejohnfreeman/lazy/badge.svg)](http://search.maven.org/#search|gav|1|g%3A%22com.thejohnfreeman%22%20AND%20a%3A%22lazy%22)
[![Javadocs](http://javadoc.io/badge/com.thejohnfreeman/lazy.svg)](http://javadoc.io/doc/com.thejohnfreeman/lazy)
[![Build Status](https://travis-ci.org/thejohnfreeman/lazy.svg?branch=master)](https://travis-ci.org/thejohnfreeman/lazy)
[![Coverage Status](https://coveralls.io/repos/github/thejohnfreeman/lazy/badge.svg?branch=master)](https://coveralls.io/github/thejohnfreeman/lazy?branch=master)

----

## Example

An exercise from the [Dragon Book][] is in the tests.

[Dragon Book]: https://www.amazon.com/Compilers-Principles-Techniques-Tools-2nd/dp/0321486811

Given a [description of a grammar for binary floating-point literals][problem],
encode a [syntax-directed translation][solution] that can
[evaluate strings in the language][test].

[problem]: src/test/java/com/thejohnfreeman/real/package-info.java
[solution]: src/test/java/com/thejohnfreeman/real/pass/LAttributedValuePass.java
[test]: src/test/java/com/thejohnfreeman/real/RealTest.java


## Related Work

- [totallylazy](https://github.com/bodar/totallylazy)

  Actively developed, but almost no documentation at the moment. Seems
  limited to just [Traversables][].

  [Traversables]: https://hackage.haskell.org/package/base-4.9.1.0/docs/Data-Traversable.html

- [java.util.concurrent.CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)

  Functions limited to arity 1 or 2. Push (eager) instead of pull (lazy)
  evaluation. Unfriendly API.
