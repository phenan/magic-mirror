package com.phenan.classes

trait Divide [F[_]] extends ContravariantFunctor[F] with Semigroupal[F] {
  def contraMap [A, B] (f: A => B): F[B] => F[A]
  def divide [A, B <: Tuple, R] (a: => F[A], b: => F[B], f: R => A *: B): F[R]

  override def product [A, B <: Tuple] (a: => F[A], b: => F[B]): F[A *: B] = divide(a, b, identity)
}
