package com.phenan.syntax.invariant

import org.junit.Test
import org.junit.Assert._

import com.phenan.generic.{given _}
import com.phenan.hkd.{given _, _}
import com.phenan.std.{given _}
import com.phenan.util.{given _, _}

enum Bar {
  case Bar1 (x: String, y: Int)
  case Bar2 (n: Double)
}

class SemiringalInvariantFunctorSyntaxTest {
  @Test def testSelectFromGeneric(): Unit = {
    val x: Option[Bar] = SemiringalInvariantFunctorSyntax.select[Option, (Bar.Bar1, Bar.Bar2), Bar](HKTuple[(Bar.Bar1, Bar.Bar2), Option](None, Some(new Bar.Bar2(10.0))))
    assertEquals(x, Some(Bar.Bar2(10.0)))
  }

  @Test def testSelectNone(): Unit = {
    val x: Option[Bar] = SemiringalInvariantFunctorSyntax.select[Option, (Bar.Bar1, Bar.Bar2), Bar](HKTuple[(Bar.Bar1, Bar.Bar2), Option](None, None))
    assertEquals(x, None)
  }
}
