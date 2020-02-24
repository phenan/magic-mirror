package com.phenan.classes

trait Apply [F[_]] extends Functor[F] with Semigroupal[F] {
  def map [A, B] (f: A => B): F[A] => F[B]  
  def ap [A, B] (f: F[A => B]): F[A] => F[B]

  override def product [A, B <: Tuple] (a: => F[A], b: => F[B]): F[A *: B] = {
    ap(map((a: A) => (b: B) => a *: b)(a))(b)
  }
}
