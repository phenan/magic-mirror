package com.phenan.hkd

import com.phenan.generic.{given _}
import com.phenan.std.{given _}
import com.phenan.util.{given _, _}

import org.junit.Test
import org.junit.Assert._

case class Foobar (foo: Int, bar: String)

class HKTupleTest {
  @Test def testProductSomes(): Unit = {
    val x = HKTuple.productAll(HKTuple[(Int, String, Double), Option](Some(1), Some("foo"), Some(10.0)))
    assertEquals(Some((1, "foo", 10.0)), x)
  }

  @Test def testProductContainsNone(): Unit = {
    val x = HKTuple.productAll(HKTuple[(String, Double), Option](Some("foo"), None))
    assertEquals(None, x)
  }

  @Test def testSumOfSome (): Unit = {
    val x = HKTuple.sumAll(HKTuple[(String, Int, Double), Option](None, Some(1), Some(2.0)))
    assertEquals(Some(Coproduct.Of[(String, Int, Double)](1)), x)
  }

  @Test def testSumOfNone (): Unit = {
    val x = HKTuple.sumAll(HKTuple[(Int, String, Int, Double), Option](None, None, None, None))
    assertEquals(None, x)
  }

  @Test def testConstructSome (): Unit = {
    val x = HKTuple.construct[(Int, String), Foobar, Option](HKTuple[(Int, String), Option](Some(10), Some("foobar")))
    assertEquals(Some(Foobar(10, "foobar")), x)
  }

  @Test def testConstructNone (): Unit = {
    val x = HKTuple.construct[(Int, String), Foobar, Option](HKTuple[(Int, String), Option](Some(20), None))
    assertEquals(None, x)
  }
}
