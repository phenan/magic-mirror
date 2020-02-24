package com.phenan.classes

import com.phenan.util._

trait Decidable [F[_]] extends Divisible[F] with Semiringal[F] {
  def divide [A, B <: Tuple, R] (a: => F[A], b: => F[B], f: R => A *: B): F[R]
  def conquer [A]: F[A]  
  def choose [A, B <: Tuple, R] (a: => F[A], b: => F[Coproduct.Of[B]], f: R => Coproduct.Of[A *: B]): F[R]
  def lose [A] (f: A => CNil): F[A]

  override def sum [A, B <: Tuple] (fa: => F[A], fb: => F[Coproduct.Of[B]]): F[Coproduct.Of[A *: B]] = choose(fa, fb, identity)
  override def zero: F[CNil] = lose(identity)
}
