package com.phenan.classes

trait MonoidalInvariantFunctor [F[_]] extends Monoidal[F] with InvariantFunctor[F]
