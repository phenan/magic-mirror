package com.phenan.lens

import com.phenan.classes._

import scala.compiletime._
import scala.compiletime.ops.int._

class TupleLens [T <: Tuple, I <: Int] (getter: T => Tuple.Elem[T, I], setter: T => Tuple.Elem[T, I] => T) extends StandardLens[T, Tuple.Elem[T, I]](getter, setter)

given tupleLens [T <: Tuple, I <: Int] (using valueOf: ValueOf[I], indexCheck: I < Tuple.Size[T] =:= true) : TupleLens[T, I] = {
  def getter: T => Tuple.Elem[T, I] = { t => t.asInstanceOf[Product].productElement(valueOf.value).asInstanceOf[Tuple.Elem[T, I]] }
  def setter: T => Tuple.Elem[T, I] => T = { t => e => 
    val (init, _ *: tail) = t.splitAt(valueOf.value)
    (init ++ e *: tail).asInstanceOf[T]
  }
  new TupleLens(getter, setter)
}
