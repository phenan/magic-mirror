package com.phenan.hkd

import com.phenan.std.{given _}
import com.phenan.util.{given _, _}

import org.junit.Test
import org.junit.Assert._

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
