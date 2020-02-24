# magic-mirror
Implementation of `Generic`, `Lens`, and `HKD` in Dotty

[ ![Download](https://api.bintray.com/packages/phenan/maven/magic-mirror/images/download.svg) ](https://bintray.com/phenan/maven/magic-mirror/_latestVersion)

## Usage

You should add the following to your `build.sbt`.

```
resolvers += Resolver.jcenterRepo

libraryDependencies += "com.phenan" %% "magic-mirror" % "0.9.4"
```

This library is developped on Dotty version 0.22.0-RC1.

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
magic-mirror also support `Generic` with `Coproduct` instead of `Union` because `Coproduct` is more powerful than `Union`.
In fact, we can convert any `Coproduct` into `Union` but cannot convert `Union` into `Coproduct`.


### Sample

```
sealed trait Foo
case class Bar (a: Int, b: String) extends Foo
case class Baz (c: String) extends Foo

import com.phenan.generic.{given _, _}
import com.phenan.util._

val generic1 = summon[ProductGeneric[Bar, (Int, String)]]

val a: Bar = generic1.fromUnderlying((10, "bar"))     // Bar(10, "bar")
val b: (Int, String) = generic1.toUnderlying(a)       // (10, "bar")

val generic2 = summon[UnionGeneric[Foo, (Bar, Baz)]]

val c: Bar | Baz = generic2.toUnderlying(Baz("baz"))  // Baz("baz") : Bar | Baz
val d: Foo = generic2.fromUnderlying(c)               // Baz("baz") : Foo

val generic3 = summon[CoproductGeneric[Foo, (Bar, Baz)]]

val e: Bar |: Baz |: CNil = generic3.toUnderlying(Baz("baz"))
val f: Foo = generic3.fromUnderlying(e)
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
import com.phenan.lens.{given _}
import com.phenan.util.{given _}

val aLens = MirrorLens[Foo].a

println(aLens.get(Foo(1, "foo", 2)))     // 1
println(aLens.set(Foo(1, "foo", 2))(5))  // Foo(5, "foo", 2)

// println(aLens.set(Foo(1, "foo", 2))("foo"))  // compile error!
// val dLens = MirrorLens[Foo].d                // compile error!
```

## HKD

Higher-kinded data (`HKD`) is a data type that is parameterized by something of kind * -> *.
For example, `HKD[Foo, Option]` expresses a data type that wraps data type `Foo`, and all fields are wrapped by `Option`.
The fields of `HKD[Foo, Option]` can be accessed by regular syntax of field access, such as `hkd.bar`.
The fields of `HKD[Foo, Option]` is mutable and we can also use assignment syntax.
We can easily translate `HKD[Foo, Option]` into `Option[Foo]` by simply call `build` method.

### Sample

```
import com.phenan.hkd.{given _, _}
import com.phenan.lens.{given _}
import com.phenan.std.{given _}
import com.phenan.util.{given _}

case class Foo (a: Int, b: String)

val hkd = HKD[Foo, Option](Some(10), None)
// val hkd2 = HKD[Foo, Option](Some("hoge"), Some(10))   // compile error!

val x = hkd.a        // Some(10)
val y = hkd.b        // None
// val z = hkd.c     // compile error!

hkd.a = Some(20)
hkd.b = Some("bar")
// hkd.b = Some(10)  // compile error!

val fooOpt: Option[Foo] = hkd.build   // Some(Foo(20, "bar"))
```

## Author
[@phenan](https://twitter.com/phenan)
