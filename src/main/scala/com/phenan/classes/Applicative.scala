package com.phenan.classes

trait Applicative [F[_]] extends Apply[F] with Monoidal[F] {  
  def combine [A, B <: Tuple, R] (a: => F[A], b: => F[B], f: A *: B => R): F[R]
  def pure [A] (a: => A): F[A]

  override def map [A, B] (f: A => B): F[A] => F[B] = { (fa: F[A]) =>
    combine[A, Unit, B](fa, pure(()), (x: A *: Unit) => f(x.head))
  }
}
