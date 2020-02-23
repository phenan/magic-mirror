package com.phenan.hkd

import com.phenan.lens.{given _}
import com.phenan.std.{given _}
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

  @Test def testHKDSet (): Unit = {
    val hkd = HKD[Foo, Option](None, Some("foo"))
    assertEquals(None, hkd.a)
    assertEquals(Some("foo"), hkd.b)
    hkd.a = Some(20)
    hkd.b = None    
    assertEquals(Some(20), hkd.a)
    assertEquals(None, hkd.b)
  }

  @Test def testHKDBuildSome (): Unit = {
    val hkd = HKD[Foo, Option](Some(20), Some("bar"))
    assertEquals(Some(Foo(20, "bar")), hkd.build)
  }

  @Test def testHKDBuildNone (): Unit = {
    val hkd = HKD[Foo, Option](None, Some("bar"))
    assertEquals(None, hkd.build)
  }
}
