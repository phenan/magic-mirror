package com.phenan.generic

import org.junit.Test
import org.junit.Assert._

import com.phenan.util._

sealed trait Foo
case class Bar (a: Int, b: String) extends Foo
case class Baz (c: String) extends Foo

case object Hoge

class GenericTest {
  @Test def testProductGeneric(): Unit = {
    val generic = summon[Generic[Bar, (Int, String)]]

    val x: Bar = generic.from((10, "foo"))
    assertEquals(x, Bar(10, "foo"))

    val y: (Int, String) = generic.to(Bar(20, "bar"))
    assertEquals(y, (20, "bar"))
  }

  @Test def testSumGeneric(): Unit = {
    val generic = summon[Generic[Foo, Union[(Bar, Baz)]]]

    val x: Foo = generic.from(Bar(30, "foobar"))
    assertEquals(x, Bar(30, "foobar"))

    val y: Bar | Baz = generic.to(Baz("baz"))
    assertEquals(y, Baz("baz"))
  }

  @Test def testSingletonGeneric(): Unit = {
    val generic = summon[Generic[Hoge.type, Unit]]

    val x: Hoge.type = generic.from(())
    assertEquals(x, Hoge)

    val y: Unit = generic.to(Hoge)
    assertEquals(y, ())
  }
}
