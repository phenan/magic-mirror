package com.phenan.classes

type SemiringalInvariantFunctor [F[_]] = Semiringal[F] & InvariantFunctor[F]
