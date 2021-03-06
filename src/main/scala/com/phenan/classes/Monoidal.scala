package com.phenan.classes

trait Monoidal [F[_]] extends Semigroupal[F] {
  def product [A, B <: Tuple] (a: => F[A], b: => F[B]): F[A *: B]
  def pure [A] (a: => A): F[A]
}
