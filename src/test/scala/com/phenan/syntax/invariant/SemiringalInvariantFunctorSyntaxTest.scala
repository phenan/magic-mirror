package com.phenan.syntax.invariant

import org.junit.Test
import org.junit.Assert._

import com.phenan.classes._
import com.phenan.generic.{given _}
import com.phenan.hkd.{given _}
import com.phenan.syntax.semiringal.{given _}
import com.phenan.util.{given _, _}

given optionSemiringalInvariantFunctor : SemiringalInvariantFunctor[Option] {
  def product [A, B <: Tuple] (a: => Option[A], b: => Option[B]): Option[A *: B] = for {
    x <- a
    y <- b
  } yield x *: y

  def pure [A] (a: => A): Option[A] = Some(a)

  def zero: Option[CNil] = None

  def sum [A, B <: Tuple] (fa: => Option[A], fb: => Option[Coproduct.Of[B]]): Option[Coproduct.Of[A *: B]] = {
    fa.map(a => Coproduct.Of[A *: B](a)).orElse(fb.map(InRight(_)))
  }

  def xmap [A, B] (f: A <=> B): Option[A] <=> Option[B] = new Iso[Function1, Option[A], Option[B]] {
    def from: Option[A] => Option[B] = _.map(f.from)
    def to: Option[B] => Option[A] = _.map(f.to)
  }
}

enum Bar {
  case Bar1 (x: String, y: Int)
  case Bar2 (n: Double)
}

class SemiringalInvariantFunctorSyntaxTest {
  @Test def testSelectFromGeneric(): Unit = {
    val x: Option[Bar] = SemiringalInvariantFunctorSyntax.select[Option, (Bar.Bar1, Bar.Bar2), Bar]((None, Some(new Bar.Bar2(10.0))))
    assertEquals(x, Some(Bar.Bar2(10.0)))
  }

  @Test def testSelectNone(): Unit = {
    val x: Option[Bar] = SemiringalInvariantFunctorSyntax.select[Option, (Bar.Bar1, Bar.Bar2), Bar]((None, None))
    assertEquals(x, None)
  }
}
