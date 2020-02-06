package com.phenan.lens

import scala.compiletime._

trait TupleLens [T <: Tuple, I <: Int] extends Lens[T, Tuple.Elem[T, I]]

given TupleHeadLens [T <: NonEmptyTuple] : TupleLens[T, 0] {
  def get (a: T): Tuple.Elem[T, 0] = a.head
  def set (a: T)(b: Tuple.Elem[T, 0]): T = (b *: a.tail).asInstanceOf
}

given TupleTailLens [A, T <: Tuple, I <: Int] (given tailLens: TupleLens[T, I]) : TupleLens[A *: T, S[I]] {
  def get (a: A *: T): Tuple.Elem[A *: T, S[I]] = tailLens.get(a.tail).asInstanceOf
  def set (a: A *: T)(b: Tuple.Elem[A *: T, S[I]]): A *: T = a.head *: tailLens.set(a.tail)(b.asInstanceOf)
}
