package com.phenan.classes

import com.phenan.util._

trait Alternative [F[_]] extends Applicative[F] with Semiringal[F] {
  def combine [A, B <: Tuple, R] (a: => F[A], b: => F[B], f: A *: B => R): F[R]
  def pure [A] (a: => A): F[A]
  def branch [A, B <: Tuple, R] (a: => F[A], b: => F[Coproduct.Of[B]], f: Coproduct.Of[A *: B] => R): F[R]
  def empty [A]: F[A]

  override def sum [A, B <: Tuple] (fa: => F[A], fb: => F[Coproduct.Of[B]]): F[Coproduct.Of[A *: B]] = branch(fa, fb, identity)
  override def zero: F[CNil] = empty
}
