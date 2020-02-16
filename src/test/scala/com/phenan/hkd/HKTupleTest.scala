package com.phenan.hkd

import com.phenan.classes._
import com.phenan.util.{given _, _}

import org.junit.Test
import org.junit.Assert._

given optionalSemiringal : Semiringal[Option] {
  def product [A, B <: Tuple] (a: => Option[A], b: => Option[B]): Option[A *: B] = for {
    x <- a
    y <- b
  } yield x *: y

  def pure [A] (a: => A): Option[A] = Some(a)

  def zero: Option[CNil] = None

  def sum [A, B <: Tuple] (fa: => Option[A], fb: => Option[Coproduct.Of[B]]): Option[Coproduct.Of[A *: B]] = {
    fa.map(a => Coproduct.Of[A *: B](a)).orElse(fb.map(InRight(_)))
  }
}

class HKTupleTest {
  @Test def testProductSomes(): Unit = {
    val x = HKTuple.productAll(HKTuple[(Int, String, Double), Option](Some(1), Some("foo"), Some(10.0)))
    assertEquals(x, Some((1, "foo", 10.0)))
  }

  @Test def testProductContainsNone(): Unit = {
    val x = HKTuple.productAll(HKTuple[(String, Double), Option](Some("foo"), None))
    assertEquals(x, None)
  }

  @Test def testSumOfSome (): Unit = {
    val x = HKTuple.sumAll(HKTuple[(String, Int, Double), Option](None, Some(1), Some(2.0)))
    assertEquals(x, Some(Coproduct.Of[(String, Int, Double)](1)))
  }

  @Test def testSumOfNone (): Unit = {
    val x = HKTuple.sumAll(HKTuple[(Int, String, Int, Double), Option](None, None, None, None))
    assertEquals(x, None)
  }
}
