package com.phenan.syntax.invariant

import com.phenan.classes._

object MonoidalInvariantFunctorSyntax {
  def discardLeft [F[_], T] (fu: F[Unit], ft: F[T])(using monoidal: Monoidal[F], invariantFunctor: InvariantFunctor[F]): F[T] = {
    invariantFunctor.xmap(Iso[Function1, (Unit, T), T] (_._2)(((), _))).from(monoidal.product(fu, monoidal.product(ft, monoidal.pure(()))))    
  }

  def discardRight [F[_], T] (ft: F[T], fu: F[Unit])(using monoidal: Monoidal[F], invariantFunctor: InvariantFunctor[F]): F[T] = {
    invariantFunctor.xmap(Iso[Function1, Tuple1[T], T](_._1)(_ *: ())).from(monoidal.product(ft, fu))
  }
}
