package com.phenan.syntax.semiringal

import com.phenan.classes._
import com.phenan.util._

opaque type SemiringalSum [T <: Tuple, F[_]] = Tuple.Map[T, F] => F[Coproduct.Of[T]]

object SemiringalSum {
  def sumAll [T <: NonEmptyTuple, F[_]] (tuple: Tuple.Map[T, F])(using semiringalSum: SemiringalSum[T, F]): F[Coproduct.Of[T]] = semiringalSum(tuple)
}

given zeroSemiringalSum [F[_]] (using semiringal: Semiringal[F]): SemiringalSum[Unit, F] = {
  _ => semiringal.zero
}

given multiSemiringalSum [F[_], A, B <: Tuple] (using semiringal: Semiringal[F], sum: SemiringalSum[B, F]): SemiringalSum[A *: B, F] = {
  tuple => semiringal.sum(tuple.head, sum(tuple.tail))
}
