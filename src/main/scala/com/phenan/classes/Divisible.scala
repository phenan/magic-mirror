package com.phenan.classes

trait Divisible [F[_]] extends Divide[F] with Monoidal[F] {
  def divide [A, B <: Tuple, R] (a: => F[A], b: => F[B], f: R => A *: B): F[R]
  def conquer [A]: F[A]

  override def contraMap [A, B] (f: A => B): F[B] => F[A] = divide[B, Unit, A](_, conquer[Unit], a => Tuple1(f(a)))

  override def pure [A] (a: => A): F[A] = conquer
}
