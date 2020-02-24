package com.phenan.classes

trait MonadPlus [F[_]] extends Monad[F] with Alternative[F] {
  def flatMap [A, B] (f: A => F[B]): F[A] => F[B]
  def pure [A] (a: => A): F[A]
  def plus [A] (a: => F[A], b: => F[A]): F[A]
  def empty [A]: F[A]
}
