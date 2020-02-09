package com.phenan.lens

import org.junit.Test
import org.junit.Assert._

class TupleLensTest {
  @Test def testTuple1Test(): Unit = {
    val tuple = 100 *: ()

    val lens = summon[TupleLens[Tuple1[Int], 0]]

    assertEquals(lens.get(tuple).toLong, 100)
    assertEquals(lens.set(tuple)(200), Tuple1(200))
  }
  
  @Test def testTuple3Test(): Unit = {
    val tuple = (1, "foo", 10.0)

    val lens = summon[TupleLens[(Int, String, Double), 1]]

    assertEquals(lens.get(tuple), "foo")
    assertEquals(lens.set(tuple)("bar"), (1, "bar", 10.0))
  }
}
