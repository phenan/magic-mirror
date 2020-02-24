package com.phenan.classes

trait Monad [F[_]] extends Applicative[F] {
  def flatMap [A, B] (f: A => F[B]): F[A] => F[B]
  def pure [A] (a: => A): F[A]

  override def ap [A, B] (f: F[A => B]): F[A] => F[B] = {
    flatMap((a: A) => flatMap((g: A => B) => pure(g(a)))(f))
  }
}
