package com.phenan.classes

import com.phenan.util._

trait Alternative [F[_]] extends Applicative[F] with Semiringal[F] {
  def combine [A, B <: Tuple, R] (a: => F[A], b: => F[B], f: A *: B => R): F[R]
  def pure [A] (a: => A): F[A]
  def plus [A] (a: => F[A], b: => F[A]): F[A]
  def empty [A]: F[A]

  override def sum [A, B <: Tuple] (fa: => F[A], fb: => F[Coproduct.Of[B]]): F[Coproduct.Of[A *: B]] = {
    plus(map((a: A) => InLeft[A, Coproduct.Of[B]](a))(fa), map((b: Coproduct.Of[B]) => InRight[A, Coproduct.Of[B]](b))(fb))
  }
  override def zero: F[CNil] = empty
}
