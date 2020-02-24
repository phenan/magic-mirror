package com.phenan.classes

trait Monad [F[_]] extends Applicative[F] {
  def flatMap [A, B] (f: A => F[B]): F[A] => F[B]
  def pure [A] (a: => A): F[A]

  override def combine [A, B <: Tuple, R] (a: => F[A], b: => F[B], f: A *: B => R): F[R] = {
    flatMap((a: A) => flatMap((b: B) => pure(f(a *: b)))(b))(a)
  }
}
