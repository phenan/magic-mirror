package com.phenan.syntax.semiringal

import com.phenan.classes._
import com.phenan.util._

import org.junit.Test
import org.junit.Assert._

given optionalSemiringal : Semiringal[Option] {
  def product [A, B <: Tuple] (a: => Option[A], b: => Option[B]): Option[A *: B] = for {
    x <- a
    y <- b
  } yield x *: y

  def pure [A] (a: => A): Option[A] = Some(a)

  def sum [A, B] (a: => Option[A], b: => Option[B]): Option[A | B] = a.orElse(b)
  
  def zero: Option[Void] = None
}

class SemiringalSumTest {
  @Test def testSumOfSome (): Unit = {
    val x = SemiringalSum.sumAll[(String, Int, Int), Option]((None, Some(1), Some(2)))
    assertEquals(x, Some(1))
  }

  @Test def testSumOfNone (): Unit = {
    val x = SemiringalSum.sumAll[(Int, String, Int, Double), Option]((None, None, None, None))
    assertEquals(x, None)
  }
}
