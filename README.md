# magic-mirror
Implementation of `Generic` and `Lens` in Dotty

[ ![Download](https://api.bintray.com/packages/phenan/maven/magic-mirror/images/download.svg) ](https://bintray.com/phenan/maven/magic-mirror/_latestVersion)

## Usage

You should add the following to your `build.sbt`.

```
resolvers += Resolver.jcenterRepo

libraryDependencies += "com.phenan" %% "dotty-generic" % "0.3.0"
```

## Generic

In Scala2, [shapeless](https://github.com/milessabin/shapeless) provides `Generic`.
`Generic` is a type class that expresses interconversion between algebraic data type and simple data type expressed with product and coproduct.
Shapeless implements it with macros since Scala2 language does not provide any useful information about such interconversion.

Dotty supports `Mirror` type class for type class derivation.
The `Mirror` provides information about how to construct algebraic data type from product of its elements, so we can implement `Generic` without macros by utilizing `Mirror`.

### Sample

```
sealed trait Foo
case class Bar (a: Int, b: String) extends Foo
case class Baz (c: String) extends Foo

import com.phenan.coproduct._
import com.phenan.generic._
import com.phenan.generic.given

val generic = summon[Generic[Bar, (Int, String)]]

val x: Bar = generic.from((10, "bar"))   // Bar(10, "bar")
val y: (Int, String) = generic.to(x)     // (10, "bar")

val z: Bar +: Baz +: CNil = generic.to(Baz("baz"))    // InR(InL(Baz("baz")))
val w: Foo = generic.from(z)                          // Baz("baz")
```

## Lens

From versin 0.3.0, magic-mirror also support `Lens`.
`Lens` is a type class expressing getter and setter of a field of a data object.

This library provides `Lens` for any field of case classes by utilizing `Mirror`.

### Sample

```
case class Foo (a: Int, b: String, c: Int)

import com.phenan.lens._

val aLens = MirrorLens[Foo, "a"]()

println(aLens.get(Foo(1, "foo", 2)))     // 1
println(aLens.set(Foo(1, "foo", 2))(5))  // Foo(5, "foo", 2)

// println(aLens.set(Foo(1, "foo", 2))("foo"))  // compile error!
// val dLens = MirrorLens[Foo, "d"]()           // compile error!
```

## Author
[@phenan](https://twitter.com/phenan)
