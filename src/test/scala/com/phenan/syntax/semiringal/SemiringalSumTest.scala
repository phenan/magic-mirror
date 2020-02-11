package com.phenan.syntax.semiringal

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

  def sum [T <: NonEmptyTuple] (tuple: Tuple.Map[T, Option]): Option[OrdinalUnion[T]] = {
    val array = tuple.toArray 
    sumHelper(array, 0, array.size)
  }

  private def sumHelper [T <: NonEmptyTuple] (tuple: Array[Object], index: Int, length: Int): Option[OrdinalUnion[T]] = {
    if (index < length) tuple(index) match {
      case Some(u) => Some(OrdinalUnion.buildUnsafe[T](u.asInstanceOf[Union[T]], index, length))
      case _       => sumHelper(tuple, index + 1, length)
    } else None
  }
}

class SemiringalSumTest {
  @Test def testSumOfSome (): Unit = {
    val x = optionalSemiringal.sum[(String, Int, Double)]((None, Some(1), Some(2.0)))
    assertEquals(x, Some(OrdinalUnion[(String, Int, Double), Int](1)))
  }

  @Test def testSumOfNone (): Unit = {
    val x = optionalSemiringal.sum[(Int, String, Int, Double)]((None, None, None, None))
    assertEquals(x, None)
  }
}
