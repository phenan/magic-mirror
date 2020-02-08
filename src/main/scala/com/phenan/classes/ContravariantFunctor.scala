package com.phenan.classes

trait ContravariantFunctor [F[_]] extends InvariantFunctor[F] {
  def contraMap [A, B] (f: A => B): F[B] => F[A]

  override def xmap [A, B] (f: A <=> B): F[A] <=> F[B] = Iso(contraMap(f.to))(contraMap(f.from))
}