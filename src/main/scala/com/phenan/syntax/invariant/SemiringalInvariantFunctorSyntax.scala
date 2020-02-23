package com.phenan.syntax.invariant

import com.phenan.classes._

import com.phenan.generic.{given _, _}
import com.phenan.hkd.{given _, _}

object SemiringalInvariantFunctorSyntax {
  def rep [F[_] : Semiringal : InvariantFunctor, T] (ft: F[T]): F[List[T]] = {
    HKTuple.bundle[List[T]](
      HKTuple[(::[T], scala.collection.immutable.Nil.type), F](
        HKTuple.construct[::[T]](HKTuple[(T, List[T]), F](ft, rep(ft))),
        HKTuple.construct[scala.collection.immutable.Nil.type](HKTuple[Unit, F](()))
      )
    )
  }
}

