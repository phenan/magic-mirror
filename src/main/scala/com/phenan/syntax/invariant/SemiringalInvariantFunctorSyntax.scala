package com.phenan.syntax.invariant

import com.phenan.classes._
import com.phenan.syntax.semiringal._
import com.phenan.util._

import com.phenan.generic.{given _, _}
import com.phenan.hkd.{given _, _}
import com.phenan.syntax.monoidal._
import com.phenan.syntax.semiringal._

import MonoidalInvariantFunctorSyntax._

object SemiringalInvariantFunctorSyntax {
  def select [F[_], T <: NonEmptyTuple, R] (tuple: HKTuple[T, F])(using semiringalInvariant: SemiringalInvariantFunctor[F], tupleFoldable: HKTupleFoldable[T], coproductGeneric: CoproductGeneric[R, T]): F[R] = {
    semiringalInvariant.xmap[R, Coproduct.Of[T]](coproductGeneric.toIso).to(SemiringalSum.sumAll(tuple))
  }

  def rep [F[_], T] (ft: F[T])(using semiringalInvariant: SemiringalInvariantFunctor[F]): F[List[T]] = select(
    HKTuple[(::[T], scala.collection.immutable.Nil.type), F](
      construct(HKTuple[(T, List[T]), F](ft, rep(ft))),
      construct(HKTuple[Unit, F](()))
    )
  )
}

