package com.phenan.syntax.monoidal

import com.phenan.classes._
import com.phenan.hkd.{given _}

import org.junit.Test
import org.junit.Assert._

given optionMonoidal : Monoidal[Option] {
  def product [A, B <: Tuple] (a: => Option[A], b: => Option[B]): Option[A *: B] = for {
    x <- a
    y <- b
  } yield x *: y

  def pure [A] (a: => A): Option[A] = Some(a)
}

class MonoidalProductTest {
  @Test def testProductSomes(): Unit = {
    val x = MonoidalProduct.productAll[(Int, String, Double), Option]((Some(1), Some("foo"), Some(10.0)))
    assertEquals(x, Some((1, "foo", 10.0)))
  }

  @Test def testProductContainsNone(): Unit = {
    val x = MonoidalProduct.productAll[(String, Double), Option]((Some("foo"), None))
    assertEquals(x, None)
  }
}
