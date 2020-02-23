package com.phenan.syntax.invariant

import org.junit.Test
import org.junit.Assert._

import com.phenan.generic.{given _}
import com.phenan.hkd.{given _, _}
import com.phenan.std.{given _}

case class Foo (n: Int, s: String)

class MonoidalInvariantFunctorSyntaxTest {
  @Test def testConstructFromGeneric(): Unit = {
    val x: Option[Foo] = MonoidalInvariantFunctorSyntax.construct[Option, (Int, String), Foo](HKTuple[(Int, String), Option](Some(10), Some("foo")))
    assertEquals(x, Some(Foo(10, "foo")))
  }

  @Test def testConstructNone(): Unit = {
    val x: Option[Foo] = MonoidalInvariantFunctorSyntax.construct[Option, (Int, String), Foo](HKTuple[(Int, String), Option](None, Some("foo")))
    assertEquals(x, None)
  }
}
