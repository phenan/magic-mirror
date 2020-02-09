package com.phenan.syntax.semiringal

import com.phenan.classes._
import com.phenan.util._

opaque type SemiringalSum [T <: NonEmptyTuple, F[_]] = Tuple.Map[T, F] => F[Union[T]]

object SemiringalSum {
  def sumAll [T <: NonEmptyTuple, F[_]] (tuple: Tuple.Map[T, F])(using semiringalSum: SemiringalSum[T, F]): F[Union[T]] = semiringalSum(tuple)
}

given tuple1SemiringalSum [E, F[_]] (using semiringal: Semiringal[F]): SemiringalSum[E *: Unit, F] = { 
  tuple => tuple.head
}

given tupleNSemiringalSum [H, T <: NonEmptyTuple, F[_]] (using semiringal: Semiringal[F], restSum: SemiringalSum[T, F]): SemiringalSum[H *: T, F] = {
  tuple => semiringal.sum[H, Union[T]](tuple.head, restSum(tuple.tail)).asInstanceOf[F[Union[H *: T]]]
}
