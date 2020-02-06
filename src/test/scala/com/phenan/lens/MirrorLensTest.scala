package com.phenan.lens

import org.junit.Test
import org.junit.Assert._

case class Foo (a: Int, b: String)
case class Bar (foo: Foo, n: Int)

class MirrorLensTest {
  @Test def testMirrorLensA(): Unit = {
    val lens = MirrorLens[Foo].a

    val x: Int = lens.get(Foo(1, "foo"))
    assertEquals(x.toLong, 1L)

    val y: Foo = lens.set(Foo(1, "foo"))(10)
    assertEquals(y, Foo(10, "foo"))
  }

  @Test def testMirrorLensB(): Unit = {
    val lens = MirrorLens[Foo].b

    val x: String = lens.get(Foo(1, "foo"))
    assertEquals(x, "foo")

    val y: Foo = lens.set(Foo(1, "foo"))("bar")
    assertEquals(y, Foo(1, "bar"))
  }

  @Test def testComposeMirrorLens(): Unit = {
    val bLens = MirrorLens[Foo].b
    val fooLens = MirrorLens[Bar].foo

    val lens = bLens.compose(fooLens)

    val x: String = lens.get(Bar(Foo(2, "foobar"), 3))
    assertEquals(x, "foobar")

    val y: Bar = lens.set(Bar(Foo(2, "foobar"), 3))("hello")
    assertEquals(y, Bar(Foo(2, "hello"), 3))
  }

  @Test def testNestedMirrorLens(): Unit = {
    val lens = MirrorLens[Bar].foo.b

    val x: String = lens.get(Bar(Foo(2, "foobar"), 3))
    assertEquals(x, "foobar")

    val y: Bar = lens.set(Bar(Foo(2, "foobar"), 3))("hello")
    assertEquals(y, Bar(Foo(2, "hello"), 3))
  }
}
