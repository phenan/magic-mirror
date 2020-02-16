package com.phenan.hkd

/* HKTuple is a wrapper type of Tuple.Map.
 * We use HKTuple instead of Tuple.Map because dotty compiler cannot regard Tuple.Map[Unit, F] as a subtype of Tuple.Map[T, F].
 */
opaque type HKTuple[T <: Tuple, F[_]] = Tuple.Map[T, F]

object HKTuple {
  def apply [T <: Tuple, F[_]] (tuple: Tuple.Map[T, F]): HKTuple[T, F] = tuple

  def headOf [H, T <: Tuple, F[_]] (tuple: HKTuple[H *: T, F]): F[H] = tuple.head
  def tailOf [H, T <: Tuple, F[_]] (tuple: HKTuple[H *: T, F]): HKTuple[T, F] = tuple.tail

  def foldRight [T <: Tuple, F[_], R[_ <: Tuple]] (tuple: HKTuple[T, F], init: => R[Unit], f: [e, es <: Tuple] => (F[e], R[es]) => R[e *: es])(using foldable: HKTupleFoldable[T]): R[T] = {
    foldable.foldRight[F, R](tuple, init, f)
  }
}

trait HKTupleFoldable [T <: Tuple] {
  def foldRight [F[_], R[_ <: Tuple]] (tuple: HKTuple[T, F], init: => R[Unit], f: [e, es <: Tuple] => (F[e], R[es]) => R[e *: es]): R[T]
}

given unitHKTupleFoldable : HKTupleFoldable[Unit] {
  def foldRight [F[_], R[_ <: Tuple]] (tuple: HKTuple[Unit, F], init: => R[Unit], f: [e, es <: Tuple] => (F[e], R[es]) => R[e *: es]): R[Unit] = init
}

given nonEmptyHKTupleFoldable [H, T <: Tuple] (using foldable: HKTupleFoldable[T]): HKTupleFoldable[H *: T] {
  def foldRight [F[_], R[_ <: Tuple]] (tuple: HKTuple[H *: T, F], init: => R[Unit], f: [e, es <: Tuple] => (F[e], R[es]) => R[e *: es]): R[H *: T] = {
    f[H, T](HKTuple.headOf(tuple), foldable.foldRight(HKTuple.tailOf(tuple), init, f.asInstanceOf))
  }
}
