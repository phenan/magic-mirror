package com.phenan.syntax.monoidal

import com.phenan.classes._
import com.phenan.hkd._

object MonoidalProduct {
  def productAll [T <: Tuple, F[_]] (tuple: HKTuple[T, F])(using monoidal: Monoidal[F], foldable: HKTupleFoldable[T]): F[T] = {
    HKTuple.foldRight[T, F, F](tuple, monoidal.pure(()), [e, es <: Tuple] => (value: F[e], accum: F[es]) => monoidal.product(value, accum))
  }
}
