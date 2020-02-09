package com.phenan.classes

trait SemiringalInvariantFunctor [F[_]] extends Semiringal[F] with MonoidalInvariantFunctor[F]
