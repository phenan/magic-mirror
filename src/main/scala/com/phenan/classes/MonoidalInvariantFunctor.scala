package com.phenan.classes

type MonoidalInvariantFunctor [F[_]] = Monoidal[F] & InvariantFunctor[F]
