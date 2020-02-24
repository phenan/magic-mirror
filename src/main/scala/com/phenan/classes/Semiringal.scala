package com.phenan.classes

import com.phenan.util._

trait Semiringal [F[_]] extends Monoidal[F] {
  def product [A, B <: Tuple] (a: => F[A], b: => F[B]): F[A *: B]
  def pure [A] (a: => A): F[A]
  def sum [A, B <: Tuple] (fa: => F[A], fb: => F[Coproduct.Of[B]]): F[Coproduct.Of[A *: B]]
  def zero: F[CNil]
}
