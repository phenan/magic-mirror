package com.phenan.syntax.semiringal

import com.phenan.classes._
import com.phenan.hkd._
import com.phenan.util._

object SemiringalSum {
  def sumAll [T <: NonEmptyTuple, F[_]] (tuple: HKTuple[T, F])(using semiringal: Semiringal[F], foldable: HKTupleFoldable[T]): F[Coproduct.Of[T]] = {
    HKTuple.foldRight[T, F, [x <: Tuple] =>> F[Coproduct.Of[x]]](tuple, semiringal.zero, new HKTuple.FoldBody[F, [x <: Tuple] =>> F[Coproduct.Of[x]]] {
      def apply [H, T <: Tuple] (value: F[H], accum: F[Coproduct.Of[T]]): F[Coproduct.Of[H *: T]] = {
        semiringal.sum(value, accum)
      }
    })
  }
}
