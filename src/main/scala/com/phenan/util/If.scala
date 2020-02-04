package com.phenan.util

type IfEq [A, B, T, F] <: (T | F) = A match {
  case B => T
  case _ => F
}
