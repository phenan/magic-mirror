package com.phenan.lens

import com.phenan.classes._

import scala.compiletime._

class TupleLens [T <: Tuple, I <: Int] (getter: T => Tuple.Elem[T, I], setter: T => Tuple.Elem[T, I] => T) extends StandardLens[T, Tuple.Elem[T, I]](getter, setter)

given TupleHeadLens [H, T <: Tuple] : TupleLens[H *: T, 0] = {
  new TupleLens[H *: T, 0] (a => a.head, a => b => (b *: a.tail))
}

given TupleTailLens [A, T <: Tuple, I <: Int] (using tailLens: TupleLens[T, I]) : TupleLens[A *: T, S[I]] = {
  new TupleLens[A *: T, S[I]] (a => tailLens.get(a.tail).asInstanceOf, a => b => a.head *: tailLens.set(a.tail)(b.asInstanceOf))
}
