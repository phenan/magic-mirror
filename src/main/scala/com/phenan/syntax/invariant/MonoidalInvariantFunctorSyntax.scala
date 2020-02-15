package com.phenan.syntax.invariant

import com.phenan.classes._
import com.phenan.generic._
import com.phenan.syntax.monoidal._

object MonoidalInvariantFunctorSyntax {
  def discardLeft [F[_], T] (fu: F[Unit], ft: F[T])(using monoidalInvariant: MonoidalInvariantFunctor[F]): F[T] = {
    monoidalInvariant.xmap(Iso[Function1, (Unit, T), T] (_._2)(((), _))).from(monoidalInvariant.product(fu, monoidalInvariant.product(ft, monoidalInvariant.pure(()))))    
  }

  def discardRight [F[_], T] (ft: F[T], fu: F[Unit])(using monoidalInvariant: MonoidalInvariantFunctor[F]): F[T] = {
    monoidalInvariant.xmap(Iso[Function1, Tuple1[T], T](_._1)(_ *: ())).from(monoidalInvariant.product(ft, fu))
  }

  def construct [F[_], T <: Tuple, R <: Product] (tuple: Tuple.Map[T, F])(using monoidalInvariant: MonoidalInvariantFunctor[F], monoidalProduct: MonoidalProduct[T, F], productGeneric: ProductGeneric[R, T]): F[R] = {    
    monoidalInvariant.xmap[R, T](productGeneric.toIso).to(MonoidalProduct.productAll[T, F](tuple)(using monoidalProduct))
  }
}
