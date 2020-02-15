package com.phenan.syntax.invariant

import com.phenan.classes._
import com.phenan.syntax.semiringal._
import com.phenan.util._

import com.phenan.generic.{given _, _}
import com.phenan.syntax.monoidal.{given _}
import com.phenan.syntax.semiringal.{given _}

import MonoidalInvariantFunctorSyntax._

object SemiringalInvariantFunctorSyntax {
  def select [F[_], T <: NonEmptyTuple, R] (tuple: Tuple.Map[T, F])(using semiringalInvariant: SemiringalInvariantFunctor[F], semiringalSum: SemiringalSum[T, F], coproductGeneric: CoproductGeneric[R, T]): F[R] = {
    semiringalInvariant.xmap[R, Coproduct.Of[T]](coproductGeneric.toIso).to(SemiringalSum.sumAll(tuple))
  }

  def rep [F[_], T] (ft: F[T])(using semiringalInvariant: SemiringalInvariantFunctor[F]): F[List[T]] = {
    select [F, (::[T], scala.collection.immutable.Nil.type), List[T]] (
      construct[F, (T, List[T]), ::[T]](ft, rep(ft)),
      construct[F, Unit, scala.collection.immutable.Nil.type](())
    )
  }
}

