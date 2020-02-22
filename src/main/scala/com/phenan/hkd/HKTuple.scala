package com.phenan.hkd

import com.phenan.classes._
import com.phenan.lens._
import com.phenan.util._

import scala.compiletime._
import scala.compiletime.ops.int._

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

  def productAll [T <: Tuple, F[_]] (tuple: HKTuple[T, F])(using foldable: HKTupleFoldable[T], monoidal: Monoidal[F]): F[T] = {
    foldable.foldRight[F, F](tuple, monoidal.pure(()), [e, es <: Tuple] => (value: F[e], accum: F[es]) => monoidal.product(value, accum))
  }

  def sumAll [T <: NonEmptyTuple, F[_]] (tuple: HKTuple[T, F])(using foldable: HKTupleFoldable[T], semiringal: Semiringal[F]): F[Coproduct.Of[T]] = {
    foldable.foldRight[F, [x <: Tuple] =>> F[Coproduct.Of[x]]](tuple, semiringal.zero, [e, es <: Tuple] => (value: F[e], accum: F[Coproduct.Of[es]]) => semiringal.sum(value, accum))
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

class HKTupleLens [T <: Tuple, F[_], I <: Int] (getter: HKTuple[T, F] => F[Tuple.Elem[T, I]], setter: HKTuple[T, F] => F[Tuple.Elem[T, I]] => HKTuple[T, F]) extends StandardLens[HKTuple[T, F], F[Tuple.Elem[T, I]]](getter, setter)

given hkTupleLens [T <: Tuple, F[_], I <: Int] (using valueOf: ValueOf[I], indexCheck: I < Tuple.Size[T] =:= true): HKTupleLens[T, F, I] = {
  def getter: HKTuple[T, F] => F[Tuple.Elem[T, I]] = { t => 
    t.asInstanceOf[Product].productElement(valueOf.value).asInstanceOf[F[Tuple.Elem[T, I]]]
  }
  def setter: HKTuple[T, F] => F[Tuple.Elem[T, I]] => HKTuple[T, F] = { t => e => 
    val (init, _ *: tail) = t.splitAt(valueOf.value)
    (init ++ e *: tail).asInstanceOf[HKTuple[T, F]]
  }
  new HKTupleLens(getter, setter)
}
