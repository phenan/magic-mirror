package com.phenan.classes

trait Apply [F[_]] extends Functor[F] with Semigroupal[F] {
  def map [A, B] (f: A => B): F[A] => F[B]
  def combine [A, B <: Tuple, R] (a: => F[A], b: => F[B], f: A *: B => R): F[R]

  override def product [A, B <: Tuple] (a: => F[A], b: => F[B]): F[A *: B] = combine(a, b, identity)

  def ap [A, B] (f: F[A => B]): F[A] => F[B] = { (fa: F[A]) =>
    combine[A, Tuple1[A => B], B](fa, map(Tuple1(_: A => B))(f), (a, g) => g(a))
  }
}
