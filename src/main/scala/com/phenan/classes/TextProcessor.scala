package com.phenan.classes

trait TextProcessor [F[_]] extends SemiringalInvariantFunctor[F] {
  def processString (string: String): F[Unit]
}
