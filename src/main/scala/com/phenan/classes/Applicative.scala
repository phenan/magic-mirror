package com.phenan.classes

trait Applicative [F[_]] extends Apply[F] with Monoidal[F] {
  def ap [A, B] (f: F[A => B]): F[A] => F[B]
  def pure [A] (a: => A): F[A]

  override def map [A, B] (f: A => B): F[A] => F[B] = ap(pure(f))
}
