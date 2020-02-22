package com.phenan.hkd

import com.phenan.classes._
import com.phenan.lens.{given _}
import com.phenan.util.{given _, _}

import org.junit.Test
import org.junit.Assert._

case class Foo (a: Int, b: String)

class HKDTest {
  given optionMonoidalInvariantFunctor : MonoidalInvariantFunctor[Option] {
    def product [A, B <: Tuple] (a: => Option[A], b: => Option[B]): Option[A *: B] = for {
      x <- a
      y <- b
    } yield x *: y

    def pure [A] (a: => A): Option[A] = Some(a)

    def xmap [A, B] (f: A <=> B): Option[A] <=> Option[B] = new Iso[Function1, Option[A], Option[B]] {
      def from: Option[A] => Option[B] = _.map(f.from)
      def to: Option[B] => Option[A] = _.map(f.to)
    }
  }

  @Test def testHKDGet (): Unit = {
    val hkd = HKD[Foo, Option](Some(10), Some("foo"))
    assertEquals(Some(10), hkd.a)
    assertEquals(Some("foo"), hkd.b)
  }

  @Test def testHKDSet (): Unit = {
    val hkd = HKD[Foo, Option](None, Some("foo"))
    assertEquals(None, hkd.a)
    assertEquals(Some("foo"), hkd.b)
    hkd.a = Some(20)
    hkd.b = None    
    assertEquals(Some(20), hkd.a)
    assertEquals(None, hkd.b)
  }

  @Test def testHKDBuildSome (): Unit = {
    val hkd = HKD[Foo, Option](Some(20), Some("bar"))
    assertEquals(Some(Foo(20, "bar")), hkd.build)
  }

  @Test def testHKDBuildNone (): Unit = {
    val hkd = HKD[Foo, Option](None, Some("bar"))
    assertEquals(None, hkd.build)
  }
}
