# magic-mirror
Implementation of `Generic` and `Lens` in Dotty

[ ![Download](https://api.bintray.com/packages/phenan/maven/magic-mirror/images/download.svg) ](https://bintray.com/phenan/maven/magic-mirror/_latestVersion)

## Usage

You should add the following to your `build.sbt`.

```
resolvers += Resolver.jcenterRepo

libraryDependencies += "com.phenan" %% "magic-mirror" % "0.6.1"
```

## Generic

`Generic` is a type class that expresses interconversion between algebraic data type and simple data type expressed with product and union.
It is useful for various situation. 
For example, we can easily implement serializer and deserializer with `Generic` because we can construct and destruct different data types in the same fashion.

In Scala2, [shapeless](https://github.com/milessabin/shapeless) provides implementation of `Generic`.
`Generic` in shapeless is implemented by macros, and the deconstructed data type is expressed by `HList` and `Coproduct`.
This is also useful, however, `HList` and `Coproduct` are not common in Scala, so it is not convenient for normal users.

magic-mirror provides `Generic` that is based on `Mirror`.
`Mirror` is a reflection API in Dotty.
The deconstructed data type of `Generic` in magic-mirror is expressed by `Tuple` and `Union`.
Both `Tuple` and `Union` is standard data type in Dotty, so programmers can easily to use it.

### Sample

```
sealed trait Foo
case class Bar (a: Int, b: String) extends Foo
case class Baz (c: String) extends Foo

import com.phenan.generic._
import com.phenan.util._
import com.phenan.generic.given

val generic1 = summon[Generic[Bar, (Int, String)]]

val x: Bar = generic1.from((10, "bar"))     // Bar(10, "bar")
val y: (Int, String) = generic1.to(x)       // (10, "bar")

val generic2 = summon[Generic[Foo, Union[(Bar, Baz)]]]

val z: Bar | Baz = generic2.to(Baz("baz"))  // Baz("baz") : Bar | Baz
val w: Foo = generic2.from(z)               // Baz("baz") : Foo
```

## Lens

magic-mirror also support `Lens`.
`Lens` is a type class expressing getter and setter of a field of a data object.

This library provides `Lens` for any field of case classes by utilizing `Mirror`.

If you want to get `Lens` for field `x` of algebraic data type `Foo`, you should simply call `MirrorLens[Foo].x`.
You can also get `Lens` of nested object with this dot-notation.
For example, `MirrorLens[Foo].bar.baz` returns `Lens[Foo, Baz]` for the following definition.

```
case class Foo (bar: Bar, x: Int)
case class Bar (baz: baz)
case class Baz (n: Int, s: String)
```

### Sample

```
case class Foo (a: Int, b: String, c: Int)

import com.phenan.lens._
import com.phenan.lens.given

val aLens = MirrorLens[Foo].a

println(aLens.get(Foo(1, "foo", 2)))     // 1
println(aLens.set(Foo(1, "foo", 2))(5))  // Foo(5, "foo", 2)

// println(aLens.set(Foo(1, "foo", 2))("foo"))  // compile error!
// val dLens = MirrorLens[Foo].d                // compile error!
```

## Author
[@phenan](https://twitter.com/phenan)
