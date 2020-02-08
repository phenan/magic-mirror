package com.phenan.classes

trait Functor [F[_]] extends InvariantFunctor[F] {
  def map [A, B] (f: A => B): F[A] => F[B]

  def [A, B] (fa: F[A]) map (f: A => B): F[B] = map(f)(fa)

  override def xmap [A, B] (f: A <=> B): F[A] <=> F[B] = Iso(map(f.from))(map(f.to))
}
