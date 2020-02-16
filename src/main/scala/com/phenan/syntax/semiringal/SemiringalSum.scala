package com.phenan.syntax.semiringal

import com.phenan.classes._
import com.phenan.hkd._
import com.phenan.util._

object SemiringalSum {
  def sumAll [T <: NonEmptyTuple, F[_]] (tuple: HKTuple[T, F])(using semiringal: Semiringal[F], foldable: HKTupleFoldable[T]): F[Coproduct.Of[T]] = {
    HKTuple.foldRight[T, F, [x <: Tuple] =>> F[Coproduct.Of[x]]](tuple, semiringal.zero, [e, es <: Tuple] => (value: F[e], accum: F[Coproduct.Of[es]]) => semiringal.sum(value, accum))
  }
}
