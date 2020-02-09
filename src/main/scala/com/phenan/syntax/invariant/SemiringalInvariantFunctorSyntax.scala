package com.phenan.syntax.invariant

import com.phenan.classes._
import com.phenan.syntax.semiringal._
import com.phenan.util._

object SemiringalInvariantFunctorSyntax {
  def select [F[_], T <: NonEmptyTuple, R] (tuple: Tuple.Map[T, F])(using semiringalInvariant: SemiringalInvariantFunctor[F], semiringalSum: SemiringalSum[T, F], iso: Union[T] <=> R): F[R] = {
    semiringalInvariant.xmap[Union[T], R](iso).from(SemiringalSum.sumAll[T, F](tuple)(using semiringalSum))
  }
}

