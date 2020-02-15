package com.phenan.syntax.invariant

import org.junit.Test
import org.junit.Assert._

import com.phenan.classes._
import com.phenan.generic.{given _}
import com.phenan.syntax.monoidal.{given _}

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

case class Foo (n: Int, s: String)

class MonoidalInvariantFunctorSyntaxTest {
  @Test def testConstructFromGeneric(): Unit = {
    val x: Option[Foo] = MonoidalInvariantFunctorSyntax.construct[Option, (Int, String), Foo]((Some(10), Some("foo")))
    assertEquals(x, Some(Foo(10, "foo")))
  }

  @Test def testConstructNone(): Unit = {
    val x: Option[Foo] = MonoidalInvariantFunctorSyntax.construct[Option, (Int, String), Foo]((None, Some("foo")))
    assertEquals(x, None)
  }
}
