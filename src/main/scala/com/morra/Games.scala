package com.morra

import RockPaperScissorsGame.{Rock, Paper, Scissors}

/**
 *  Factory collecting all [[Game]] instances supported by the application
 */
object Games {
  def RockPaperScissors() : Game = new RockPaperScissorsGame()
}

/**
 * The base trait of the signs involved in the game
 */
trait Sign

/**
 * It collects the signs involved in a game hand
 */
case class Hand(sign1: Sign, sign2: Sign)

/**
 * A trait hosting the implementation of the game rules.
 * The play PartialFunction returning an instance of [[Win]] if one between sign1 or sign2 wins, [[Draw]] otherwise.
 *
 * Extends the [[Game]] trait to implement a new game:
 * {{{
 * class FakeGame extends Game {
 *   override def play = {
 *     case Hand(Fake1, Fake2) => Win(Fake1)
 *     case Hand(Fake2, Fake1) => Win(Fake1)
 *     case _ => Draw
 *   }
 *   override def signs: Set[Sign] = Set(Fake1, Fake2, Fake3)
 * }
 * }}}
 */
trait Game {
  type Evaluate = PartialFunction[Hand, Result[Sign]]

  def play : Evaluate
  def play(sign1: Sign, sign2: Sign) : Result[Sign] = play(Hand(sign1, sign2))

  /**
   * The signs involved in the game
   */
  def signs : Set[Sign]
}

/**
 * The companion object declaring the signs used by the game Rock, Paper, Scissors
 */
object RockPaperScissorsGame {
  case object Rock extends Sign
  case object Scissors extends Sign
  case object Paper extends Sign
}

/**
 * The game [[http://en.wikipedia.org/wiki/Rock-paper-scissors Rock, Paper, Scissors]]
 *
 * The rules are:
 *
 *  - sign1 equal to sign2 -> Draw
 *  - Rock vs Scissors -> Rock win
 *  - Rock vs Paper -> Paper win
 *  - Scissors vs Paper -> Scissors win
 */
class RockPaperScissorsGame extends Game {
  def play = {
    case Hand(sign1, sign2) if sign1 == sign2 && signs.contains(sign1) => Draw
    case Hand(Rock, Scissors)  => Win(Rock)
    case Hand(Rock, Paper)  => Win(Paper)
    case Hand(Paper, Scissors)  => Win(Scissors)
    case Hand(Paper, Rock)  => Win(Paper)
    case Hand(Scissors, Paper)  => Win(Scissors)
    case Hand(Scissors, Rock)  => Win(Rock)
  }

  override def signs: Set[Sign] = Set(Rock, Paper, Scissors)
}