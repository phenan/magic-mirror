# dotty-generic
Implementation of `Generic` in Dotty

[ ![Download](https://api.bintray.com/packages/phenan/maven/dotty-generic/images/download.svg) ](https://bintray.com/phenan/maven/dotty-generic/_latestVersion)

In Scala2, [shapeless](https://github.com/milessabin/shapeless) provides `Generic`.
`Generic` is a type class that expresses interconversion between algebraic data type and simple data type that is expressed with product and coproduct.
Shapeless implements it with macros since Scala2 language does not provide any useful information about such interconversion.

Dotty supports `Mirror` type class for supporting type class derivation.
The `Mirror` provides information about how to construct algebraic data type from product of its elements, so we can implement `Generic` without macros by utilizing `Mirror`.

## Usage

You should add the following to your `build.sbt`.

```
resolvers += Resolver.jcenterRepo

libraryDependencies += "com.phenan" %% "dotty-generic" % "0.1.0"
```

## Sample

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

val z: Bar :+: Baz :+: CNil = generic.to(Baz("baz"))    // InR(InL(Baz("baz")))
val w: Foo = generic.from(z)                            // Baz("baz")
```

## Author
[@phenan](https://twitter.com/phenan)
