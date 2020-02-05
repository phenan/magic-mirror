package com.phenan.lens

import org.junit.Test
import org.junit.Assert._

case class Foo (a: Int, b: String)

class MirrorLensTest {
  @Test def testMirrorLensA(): Unit = {
    val lens = MirrorLens[Foo, "a"]()

    val x: Int = lens.get(Foo(1, "foo"))
    assertEquals(x.toLong, 1L)

    val y: Foo = lens.set(Foo(1, "foo"))(10)
    assertEquals(y, Foo(10, "foo"))
  }

  @Test def testMirrorLensB(): Unit = {
    val lens = MirrorLens[Foo, "b"]()

    val x: String = lens.get(Foo(1, "foo"))
    assertEquals(x, "foo")

    val y: Foo = lens.set(Foo(1, "foo"))("bar")
    assertEquals(y, Foo(1, "bar"))
  }
}
