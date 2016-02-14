package com.morra

import scala.util.Random

/**
 *  Factory for all [[User]] instances
 */
object Users {
  def Computer(implicit moveStrategy: MoveStrategy) : Computer = new Computer()
  def Player(sign: Sign) : Player = new Player(sign)
}

/**
 * Who can play a [[Game]]
 */
sealed trait User {
  def move(): Sign
}

/**
 * The [[User]] able to play without any input interaction.
 *
 * The [[Computer]] receives a pluggable strategy in order to support different Sign distribution
 */
final class Computer(implicit val moveStrategy: MoveStrategy) extends User {
  override def move(): Sign = moveStrategy.next()
}

/**
 * The [[User]] who accepts the move to play from the external
 */
final class Player(val next : Sign) extends User {
  override def move(): Sign = next
}

/**
 * The trait of the strategy used by a [[Computer]]
 */
trait MoveStrategy {
  def next(): Sign
}

/**
 * The random move strategy used by the [[Computer]]
 */
class RandomMoveStrategy(signs: Set[Sign]) extends MoveStrategy {
  override def next(): Sign = signs.toVector(Random.nextInt(signs.size))
}

