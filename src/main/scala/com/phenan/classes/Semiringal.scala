package com.phenan.classes

import com.phenan.util._

trait Semiringal [F[_]] extends Monoidal[F] {
  def sum [T <: NonEmptyTuple] (tuple: Tuple.Map[T, F]): F[OrdinalUnion[T]]
}
