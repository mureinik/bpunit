# BPUnit
## Boilerplate Utilities for JUnit

### What is BPUnit?
BPUnit (short for Boiler Plate Unit) is a set of utilities to help
alleviate the pain of boiler-plating your [JUnit](http://junit.org)
tests.

### Why do I need BPUnit?

> The opposite of making a mistake is testing.

  Alleh, N. (2004).

All too often you encounter a situation where some piece of the code was
not tested because "it's too simple to break", or "it's not worth the
effort". More often than not, these are the places that contain the
bugs.

BPUnit attempts to fill this gap by offering a set of utilities to make
testing even these boring parts of the code as pain free as possible.

### Using BPUnit

#### The Basics: `POJOAsserter` and `POJOAsserterBuilder`

The `POJOAsserter` class does exactly what its name says - it asserts a
POJO. Well, its properties, at least.
`POJOAsserters` aren't created directly, they are built with a 
`POJOAsserterBuilder`, unsurprisingly. The basic flow, detailed below, 
will iterate over all of the object's properties, `set` each one
with a random value, `get` it back, and compare the two.  If any of
these values don't match, the test will fail:

    new POJOAsserterBuilder().forPOJO(myObject).build().assertProperties();

#### Changing behavior with `POJOAsserterBuilder`

`POJOAsserterBuilder` is a fluent API to create `POJOAsserter`
instances. It strives to assume as little as possible about the
assertion behavior you'd want by allowing you to configure the
`POJOAsserter` with a series of `withXYZ` methods. Currently, there are
three behaviors that can be configured:

  1. Handling a situation where your POJO class has a setter method such
     as `setSomething(SomeClass)`, but does not have a corresponding
     getter (`getSomething()`, in this example). 
     This behavior can be influced by using the
     `withNoGetterBehavior(Behavior)` method.
     If not explicitly configured, the default behavior is to log such
     incidents, and continue the test.
  2. Handling a situation where you cannot test a getter/setter pair
     (e.g., the getter throws an exception).
     This behavior can be influced by using the 
     `withPropertyTestFailureBehavior(Behavior)` method.
     If not explicitly configured, the default behavior is to fail the
     test.
  3. Handling a situation where your `Radnom` generator can't generate a
     random value for one of your POJO's properties (e.g., it doesn't 
     have the right method).
     If not explicitly configured, the default behavior is to log such
     incidents, and continue the test.

E.g.:

     new POJOAsserterBuilder().forPOJO(myObject)
                              .withNoGetterBahvior(new FailureBehavior())
                              .build()
                              .assertProperties();


#### Built-in `Behavior`s

BPUnit provides several simple built-in behaviors to use with
`POJOAsserterBuilder`:

  1. `LoggingBehavior` - logs a message and continues the assertion flow.
  2. `FailingBehavior` - fails the assertion flow.
  3. `ThrowingBehavior` - throws an `IllegalArguemntException` 

 
#### Using a different `Random`

While [`java.util.Random`](http://docs.oracle.com/javase/8/docs/api/java/util/Random.html)
is a neat little randomizer, it has several drawbacks. First and
foremost, it can only randomize a small subset of the JDK's types, 
whereas the classes you'd want to test usually have properties from your
own classes.
`POJOAssertBuilder#withRadnom(Random)` allows you extend
`java.util.Random` and provide methods to randomize your own classes. A
randomizer for `MyClass` is a method with the following properties:
  
  1. It's public.  
  2. Its return type is `MyClass`.  
  3. Its called `nextMyClass`.  
  4. It doesn't have any arguments.

These new extensions of `Random` can be used with a `POJOAsserter` by
calling the `withRandom` method when building it. If not explicitly
specified, the `SeedableRandom` class described below is used.

     new POJOAsserterBuilder().forPOJO(myObject).withRandom(new MyRandom()).build().assertProperties();

#### The Basics: Reproducible tests

A test that cannot be reproduced probably has a limited value, and at
first glance using random values in a test does not lend to that notion.
Fortunately, since Java can only really guarantee a pseudo-random
number, even these so called "random" numbers can be generated in a
predictable way by controlling the seed. This is where `SeedableRandom`
comes in to play. It's seed can be specified from the code by using
`SeedableRandom(long)` or externally by using the default constructor
`SeedableRandom()` and setting the `BPUNIT.SEED` environment variable. 
In any event, the seed will be logged so that it can be re-injected in
case a test fails.


### Maintaining BPUnit

The general philosophy behind BPUnit is that it should be as lightweight
and as non-intrusive as possible. Under this philosophy, BPUnit should
not depend on any other external libraries (except for
[JUnit](http://junit.org/), of course). This is why `SeedableRandom`
re-implements several methods present in
[Apache Commons Lang](http://commons.apache.org/proper/commons-lang/),
and why `AssertUtils` does not use [Apache Commons
BeanUtils](http://commons.apache.org/proper/commons-beanutils/) or
anything of the sort. However, it's pretty hard to keep the dependency
list completely empty and some were introduced:

  1. Obviously, as a set of utilities to helper for JUnit, BPUnit must
     depend on it. BPUnit depends on JUnit Jupiter 5.3.2, the latest
     JUnit release.
     JUnit does not break backwards compatibility between minor
     versions, and although BPUnit does *not* guarantee it, it strive
     to use only the the most trivial APIs JUnit provides in order to
     be backwards compatible with older JUnit 5.x releases.

  2. [SLF4J](http://www.slf4j.org/) is used to hide the logging
     implementation details. This is done to allow other projects using
     BPUnit to easily integrate its logging with theirs by dropping in
     the appropriate adapter and supplying it with the proper
     configuration.
     BPUnit's tests run using the
     [simple implementation](http://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html)
     in order to eliminate the need for an additional 3rd party jar but
     the "production" code is not aware of this fact, and, as noted, it
     can easily be replaced.
     BPUnit uses the `slf4j-api` and `slf4j-simple` libraries from the
     1.7.21 release in order to incorporate bug fixes and implementation
     improvements. However, note that it only uses trivial syntax, and
     would be binary compatible with any slf4j binary from the 1.7.x
     generation, if not earlier.

### License
BPUnit is licensed under the LGPL-3 license. See
[LICENSE.md](LICENSE.md) for additional details. 

### Continuous Integration
[![Build Status](https://travis-ci.org/mureinik/bpunit.svg?branch=master)](https://travis-ci.org/mureinik/bpunit)
[![Coverage Status](https://coveralls.io/repos/github/mureinik/bpunit/badge.svg?branch=master)](https://coveralls.io/github/mureinik/bpunit?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c40eb7ecea6e4d5aa482f61849c34ad5)](https://www.codacy.com/app/mureinik/bpunit?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mureinik/bpunit&amp;utm_campaign=Badge_Grade)
[![Coverity Scan](https://scan.coverity.com/projects/3157/badge.svg)](https://scan.coverity.com/projects/bpunit)
