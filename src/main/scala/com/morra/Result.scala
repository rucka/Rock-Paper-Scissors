package com.morra

/**
 * The result of a game hand played by Users. A [[Result]] could be either [[Win]] or [[Draw]]
 */
sealed abstract class Result[+P <: Sign] {
  def isDraw: Boolean
  def get: P
}

/**
 * The victory of a Sign
 */
final case class Win[+S <: Sign](x: S) extends Result[S] {
  def isDraw = false
  def get = x
}

/**
 * Represents the Draw result between two signs
 */
case object Draw extends Result[Nothing] {
  def isDraw = true
  def get = throw new NoSuchElementException("Draw.get")
}

