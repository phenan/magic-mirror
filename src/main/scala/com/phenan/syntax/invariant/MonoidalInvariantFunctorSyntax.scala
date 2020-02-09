package com.phenan.syntax.invariant

import com.phenan.classes._
import com.phenan.syntax.monoidal._

object MonoidalInvariantFunctorSyntax {
  def construct [F[_], T <: Tuple, R] (tuple: Tuple.Map[T, F])(using monoidalInvariant: MonoidalInvariantFunctor[F], monoidalProduct: MonoidalProduct[T, F], iso: T <=> R): F[R] = {    
    monoidalInvariant.xmap[T, R](iso).from(MonoidalProduct.productAll[T, F](tuple)(using monoidalProduct))
  }
}
