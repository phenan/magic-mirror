package com.phenan.hkd

import com.phenan.lens.{given _}
import com.phenan.util.{given _, _}

import org.junit.Test
import org.junit.Assert._

case class Foo (a: Int, b: String)

class HKDTest {
  @Test def testHKDGet (): Unit = {
    val hkd = HKD[Foo, Option](Some(10), Some("foo"))
    assertEquals(Some(10), hkd.a)
    assertEquals(Some("foo"), hkd.b)
  }
}
