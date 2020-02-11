package com.phenan.syntax.textprocessor

import com.phenan.classes.{given _, _}
import com.phenan.generic.{given _}
import com.phenan.syntax.textprocessor._
import com.phenan.util._

import org.junit.Test
import org.junit.Assert._

class SimplePrinter[T] (print: T => Unit) {
  def runPrinter (value: T): Unit = print(value)
}

given simplePrinterTextProcessor : TextProcessor[SimplePrinter] {
  def xmap [A, B] (f: A <=> B): SimplePrinter[A] <=> SimplePrinter[B] = new Iso[Function1, SimplePrinter[A], SimplePrinter[B]] {
    def from: SimplePrinter[A] => SimplePrinter[B] = pa => new SimplePrinter(b => pa.runPrinter(f.to(b)))
    def to: SimplePrinter[B] => SimplePrinter[A] = pb => new SimplePrinter(a => pb.runPrinter(f.from(a)))
  }

  def product [A, B <: Tuple] (a: => SimplePrinter[A], b: => SimplePrinter[B]): SimplePrinter[A *: B] = new SimplePrinter ({ tuple =>
    a.runPrinter(tuple.head)
    b.runPrinter(tuple.tail)
  })
  
  def pure [A] (a: => A): SimplePrinter[A] = new SimplePrinter(_ => ())

  def sum [A, B <: Tuple] (fa: => SimplePrinter[A], fb: => SimplePrinter[Coproduct.Of[B]]): SimplePrinter[Coproduct.Of[A *: B]] = {
    new SimplePrinter (_.fold(fa.runPrinter)(fb.runPrinter))
  }
  
  def zero: SimplePrinter[CNil] = new SimplePrinter(_ => ())

  def processString (string: String): SimplePrinter[Unit] = new SimplePrinter[Unit] ({ _ =>
    println(string)
  })
}

case object Foo

case class Bar (foo: Foo.type)

case class Baz (foo: Foo.type, bar: Bar)

class TextProcessorTest {
  @Test def testPrintSimpleString(): Unit = {
    val foo: SimplePrinter[Foo.type] = w"foo"
    val bar: SimplePrinter[Bar] = w"bar$foo"
    //val baz: SimplePrinter[Baz] = w2"baz $foo hoge $bar"
    val baz: SimplePrinter[Baz] = new StringContext("baz ", " hoge ").w2[SimplePrinter, (Foo.type, Bar), Baz](foo, bar)
    //baz.runPrinter(Baz(Foo, Bar(Foo)))
  }
}
