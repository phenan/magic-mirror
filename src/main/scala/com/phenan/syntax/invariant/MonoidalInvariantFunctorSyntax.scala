package com.phenan.syntax.invariant

import com.phenan.classes._

object MonoidalInvariantFunctorSyntax {
  def discardLeft [F[_], T] (fu: F[Unit], ft: F[T])(using monoidalInvariant: MonoidalInvariantFunctor[F]): F[T] = {
    monoidalInvariant.xmap(Iso[Function1, (Unit, T), T] (_._2)(((), _))).from(monoidalInvariant.product(fu, monoidalInvariant.product(ft, monoidalInvariant.pure(()))))    
  }

  def discardRight [F[_], T] (ft: F[T], fu: F[Unit])(using monoidalInvariant: MonoidalInvariantFunctor[F]): F[T] = {
    monoidalInvariant.xmap(Iso[Function1, Tuple1[T], T](_._1)(_ *: ())).from(monoidalInvariant.product(ft, fu))
  }
}
