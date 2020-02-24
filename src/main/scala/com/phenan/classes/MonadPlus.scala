package com.phenan.classes

import com.phenan.util._

trait MonadPlus [F[_]] extends Monad[F] with Alternative[F] {
  def flatMap [A, B] (f: A => F[B]): F[A] => F[B]
  def pure [A] (a: => A): F[A]
  def branch [A, B <: Tuple, R] (a: => F[A], b: => F[Coproduct.Of[B]], f: Coproduct.Of[A *: B] => R): F[R]
  def empty [A]: F[A]
}
