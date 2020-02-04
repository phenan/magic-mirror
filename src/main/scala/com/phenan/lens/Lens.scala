package com.phenan.lens

trait Lens [E, T] {
  def get (t: T): E
  def set (t: T)(e: E): T
}
