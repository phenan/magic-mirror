package com.phenan.syntax.monoidal

import com.phenan.classes._

opaque type MonoidalProduct [T <: Tuple, F[_]] = Tuple.Map[T, F] => F[T]

object MonoidalProduct {
  def productAll [T <: Tuple, F[_]] (tuple: Tuple.Map[T, F])(using monoidalProduct: MonoidalProduct[T, F]): F[T] = monoidalProduct(tuple)
}

given unitMonoidalProduct [F[_]] (using monoidal: Monoidal[F]): MonoidalProduct[Unit, F] = _ => monoidal.pure(())

given nonEmptyMonoidalProduct [H, T <: Tuple, F[_]] (using monoidal: Monoidal[F], tailProduct: MonoidalProduct[T, F]): MonoidalProduct[H *: T, F] = {
  tuple => monoidal.product(tuple.head, tailProduct(tuple.tail))
}
