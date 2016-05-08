# BPUnit
## Boilerplate Utilities for JUnit

### What is BPUnit?
BPUnit (short for Boiler Plate Unit) is a set of utilities to help alleviate
the pain of boiler-plating your JUnit tests.

### Why do I need BPUnit?

> The opposite of making a mistake is testing.

  Alleh, N. (2004).

All to often you encounter a situation where some piece of the code was not
tested because "it's too simple to break", or "it's not worth the effort". More
often than not, these are the places that contain the bugs.

BPUnit attempts to fill this gap by offering a set of utilities to make testing
even these boring parts of the code as pain free as possible.

### Using BPUnit

#### The Basics: `AssertUtils`

Calling `AssertUtils.testProperties(Object)` is the most basic way to use
BPUnit. It will iterate over all of the object's parameters, `set` each one
with a random value, `get` it back, and compare the two.  If any of these
properties don't match, the test will fail. Missing properties will generate an
info log, but will not fail the test.

#### The Basics: Using a different `Random`

While [`java.util.Random`](http://docs.oracle.com/javase/8/docs/api/java/util/Random.html)
is a neat little randomizer, it has several drawbacks. First and foremost, it 
can only randomize a small subset of the JDK's types, wheres the classes you'd
want to test usually have properties from your own classes.  
`AssertUtils.testProperties(Object, Random)` allows you extend
`java.util.Random` and provide methods to randomize your own classes. A
randomizer for `MyClass` is a method with the following properties:
  
  1. It's public.  
  2. Its return type is `MyClass`.  
  3. Its called `nextMyClass`.  
  4. It doesn't have any arguments.

#### The Basics: Reproducible tests

A test that cannot be reproduced probably has a limited value, and at first
glance using random values in a test does not lend to that notion.
Fortunately, since Java can only really guarantee a pseudo-random number, even
these so called "random" numbers can be generated in a predictable way by
controlling the seed. This is where `SeedableRandom` comes in to play. It's
seed can be specified from the code by using `SeedableRandom(long)` or
externally by using the default constructor `SeedableRandom()` and setting the
`BPUNIT.SEED` environment variable.  In any event, the seed will be logged so
that it can be re-injected in case a test fails.


### Maintaining BPUnit

The general philosophy behind BPUnit is that it should be as lightweight and as
non-intrusive as possible.  Under this philosophy, BPUnit should not depend on
any other external libraries (except for [JUnit](http://junit.org/), of
course). This is why `SeedableRandom` re-implements several methods present in
[Apache Commons Lang](http://commons.apache.org/proper/commons-lang/), and why
`AssertUtils` does not use [Apache Commons
BeanUtils](http://commons.apache.org/proper/commons-beanutils/) or anything of
the sort.
