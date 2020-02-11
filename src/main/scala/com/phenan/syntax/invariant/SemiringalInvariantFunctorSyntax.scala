package com.phenan.syntax.invariant

import com.phenan.classes._
import com.phenan.util._

object SemiringalInvariantFunctorSyntax {
  def select [F[_], T <: NonEmptyTuple, R] (tuple: Tuple.Map[T, F])(using semiringalInvariant: SemiringalInvariantFunctor[F], iso: OrdinalUnion[T] <=> R): F[R] = {
    semiringalInvariant.xmap[OrdinalUnion[T], R](iso).from(semiringalInvariant.sum[T](tuple))
  }
}

