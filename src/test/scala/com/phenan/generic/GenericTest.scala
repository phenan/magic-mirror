package com.phenan.generic

import org.junit.Test
import org.junit.Assert._

import com.phenan.util.{given _, _}

sealed trait Foo
case class Bar (a: Int, b: String) extends Foo
case class Baz (c: String) extends Foo

case object Hoge

class GenericTest {
  @Test def testProductGeneric(): Unit = {
    val generic = summon[ProductGeneric[Bar, (Int, String)]]

    val x: Bar = generic.fromUnderlying((10, "foo"))
    assertEquals(x, Bar(10, "foo"))

    val y: (Int, String) = generic.toUnderlying(Bar(20, "bar"))
    assertEquals(y, (20, "bar"))
  }

  @Test def testUnionGeneric(): Unit = {
    val generic = summon[UnionGeneric[Foo, (Bar, Baz)]]

    val x: Foo = generic.fromUnderlying(Bar(30, "foobar"))
    assertEquals(x, Bar(30, "foobar"))

    val y: Bar | Baz = generic.toUnderlying(Baz("baz"))
    assertEquals(y, Baz("baz"))
  }
  
  @Test def testCoproductGeneric(): Unit = {
    val generic = summon[CoproductGeneric[Foo, (Bar, Baz)]]

    val x: Foo = generic.fromUnderlying(Coproduct.Of[(Bar, Baz)](Bar(30, "foobar")))
    assertEquals(x, Bar(30, "foobar"))

    val y: Bar |: Baz |: CNil = generic.toUnderlying(Baz("baz"))
    assertEquals(y, InRight(InLeft(Baz("baz"))))
  }

  @Test def testSingletonGeneric(): Unit = {
    val generic = summon[ProductGeneric[Hoge.type, Unit]]

    val x: Hoge.type = generic.fromUnderlying(())
    assertEquals(x, Hoge)

    val y: Unit = generic.toUnderlying(Hoge)
    assertEquals(y, ())
  }

  @Test def testScalaConsGeneric(): Unit = {
    val generic = summon[ProductGeneric[::[Int], (Int, List[Int])]]

    val x = generic.fromUnderlying(1, List(2, 3))
    assertEquals(x, List(1, 2, 3))

    val y = generic.toUnderlying(::(2, List(3, 4)))
    assertEquals(y, (2, List(3, 4)))
  }

  @Test def testScalaNilGeneric(): Unit = {
    val generic = summon[ProductGeneric[scala.collection.immutable.Nil.type, Unit]]

    val x = generic.fromUnderlying(())
    assertEquals(x, Nil)

    val y = generic.toUnderlying(Nil)
    assertEquals(y, ())
  }

  @Test def testScalaListCoproductGeneric(): Unit = {
    val generic = summon[CoproductGeneric[List[Int], (::[Int], scala.collection.immutable.Nil.type)]]

    val x = generic.fromUnderlying(Coproduct.Of[(::[Int], scala.collection.immutable.Nil.type)](Nil))
    assertEquals(x, Nil)

    val y = generic.toUnderlying(List(4, 5, 6, 7))
    assertEquals(y, InLeft(::(4, List(5, 6, 7))))
  }

  @Test def testScalaListUnionGeneric(): Unit = {
    val generic = summon[UnionGeneric[List[Int], (::[Int], scala.collection.immutable.Nil.type)]]

    val x = generic.fromUnderlying(::(5, List(6)))
    assertEquals(x, List(5, 6))

    val y: ::[Int] | scala.collection.immutable.Nil.type = generic.toUnderlying(List(6, 7, 8))
    assertEquals(y, ::(6, List(7, 8)))
  }
}
