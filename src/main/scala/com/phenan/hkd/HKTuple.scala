package com.phenan.hkd

/* HKTuple is a wrapper type of Tuple.Map.
 * We use HKTuple instead of Tuple.Map because dotty compiler cannot regard Tuple.Map[Unit, F] as a subtype of Tuple.Map[T, F].
 */
opaque type HKTuple[T <: Tuple, F[_]] = Tuple.Map[T, F]

object HKTuple {
  def apply [T <: Tuple, F[_]] (tuple: Tuple.Map[T, F]): HKTuple[T, F] = tuple

  def headOf [H, T <: Tuple, F[_]] (tuple: HKTuple[H *: T, F]): F[H] = tuple.head
  def tailOf [H, T <: Tuple, F[_]] (tuple: HKTuple[H *: T, F]): HKTuple[T, F] = tuple.tail

  trait FoldBody [F[_], G[_ <: Tuple]] {
    def apply [H, T <: Tuple] (value: F[H], accum: F[G[T]]): F[G[H *: T]]
  }

  def foldRight [T <: Tuple, F[_], G[_ <: Tuple]] (tuple: HKTuple[T, F], init: => F[G[Unit]], f: FoldBody[F, G])(using foldable: HKTupleFoldable[T]): F[G[T]] = {
    foldable.foldRight[F, G](tuple, init, f)
  }
}

trait HKTupleFoldable [T <: Tuple] {
  def foldRight [F[_], G[_ <: Tuple]] (tuple: HKTuple[T, F], init: => F[G[Unit]], f: HKTuple.FoldBody[F, G]): F[G[T]]
}

given unitHKTupleFoldable : HKTupleFoldable[Unit] {
  def foldRight [F[_], G[_ <: Tuple]] (tuple: HKTuple[Unit, F], init: => F[G[Unit]], f: HKTuple.FoldBody[F, G]): F[G[Unit]] = init
}

given nonEmptyHKTupleFoldable [H, T <: Tuple] (using foldable: HKTupleFoldable[T]): HKTupleFoldable[H *: T] {
  def foldRight [F[_], G[_ <: Tuple]] (tuple: HKTuple[H *: T, F], init: => F[G[Unit]], f: HKTuple.FoldBody[F, G]): F[G[H *: T]] = {
    f(HKTuple.headOf(tuple), foldable.foldRight(HKTuple.tailOf(tuple), init, f))
  }
}
