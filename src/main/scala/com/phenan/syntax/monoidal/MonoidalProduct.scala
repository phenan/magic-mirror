package com.phenan.syntax.monoidal

import com.phenan.classes._
import com.phenan.hkd._

object MonoidalProduct {
  def productAll [T <: Tuple, F[_]] (tuple: HKTuple[T, F])(using monoidal: Monoidal[F], foldable: HKTupleFoldable[T]): F[T] = {
    HKTuple.foldRight[T, F, F](tuple, monoidal.pure(()), new HKTuple.FoldBody[F, F] {
      def apply [H, T <: Tuple] (value: F[H], accum: F[T]): F[H *: T] = {
        monoidal.product(value, accum)
      }
    })
  }
}
