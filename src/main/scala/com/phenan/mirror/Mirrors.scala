package com.phenan.mirror

import scala.deriving.Mirror

type ProductMirror [T, R <: Product] = Mirror.ProductOf[T] { type MirroredElemTypes = R }

type SumMirror [T, R] = Mirror.SumOf[T] { type MirroredElemTypes = R }

type SingletonMirror [T] = Mirror.ProductOf[T] { type MirroredElemTypes = Unit }
