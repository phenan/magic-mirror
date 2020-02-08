package com.phenan.lens

import com.phenan.classes._

import scala.compiletime._

trait TupleLens [T <: Tuple, I <: Int] extends Lens[T, Tuple.Elem[T, I]]

given TupleHeadLens [T <: NonEmptyTuple] : TupleLens[T, 0] {
  override def runLens [F[_] : Functor] (a: T, f: Tuple.Elem[T, 0] => F[Tuple.Elem[T, 0]]): F[T] = {
    f(a.head).map(e => (e *: a.tail).asInstanceOf[T])
  }

  override def get (a: T): Tuple.Elem[T, 0] = a.head
  override def set (a: T)(b: Tuple.Elem[T, 0]): T = (b *: a.tail).asInstanceOf
}

given TupleTailLens [A, T <: Tuple, I <: Int] (given tailLens: TupleLens[T, I]) : TupleLens[A *: T, S[I]] {
  override def runLens [F[_] : Functor] (a: A *: T, f: Tuple.Elem[A *: T, S[I]] => F[Tuple.Elem[A *: T, S[I]]]): F[A *: T] = {
    tailLens.runLens(a.tail, f.asInstanceOf[Tuple.Elem[T, I] => F[Tuple.Elem[T, I]]]).map(a.head *: _)
  }

  override def get (a: A *: T): Tuple.Elem[A *: T, S[I]] = tailLens.get(a.tail).asInstanceOf
  override def set (a: A *: T)(b: Tuple.Elem[A *: T, S[I]]): A *: T = a.head *: tailLens.set(a.tail)(b.asInstanceOf)
}
