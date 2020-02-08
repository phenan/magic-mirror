package com.phenan.classes

trait Functor [F[_]] extends InvariantFunctor[F] {
  def map [A, B] (f: A => B): F[A] => F[B]

  override def xmap [A, B] (f: A <=> B): F[A] <=> F[B] = Iso(map(f.from))(map(f.to))
}
