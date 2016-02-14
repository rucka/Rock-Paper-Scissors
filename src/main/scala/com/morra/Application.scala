package com.morra

import scala.annotation.tailrec

/**
 * [[Application]] companion object
 */
object Application {

  /**
   * A Game is not valid due to error {{message}}
   */
  case class InvalidGameException(message: String) extends Exception(message)

  /**
   * Given two [[User]] and a [[Game]] it collect the move from the users and play the game.
   *
   * Based on the [[Result]], the methods returns a message description of the result of the hand
   */
  def evaluate(user1: User, user2: User, game: Game) : String = {
    val sign1 = user1.move()
    val sign2 = user2.move()
    val result = game.play(sign1, sign2)
    result match {
      case Draw if user1.isInstanceOf[Player] => s"Playing $sign1 vs $sign2 .... You Draw!\nPlay again?"
      case Draw => s"Playing $sign1 vs $sign2 .... Draw!\nPlay again?"
      case Win(s) if s == sign1 && user1.isInstanceOf[Player] => s"Playing $sign1 vs $sign2 .... You Win!\nPlay again?"
      case Win(s) if s == sign2 && user1.isInstanceOf[Player] => s"Playing $sign1 vs $sign2 .... You Lost:(\nPlay again?"
      case Win(s) => s"Playing $sign1 vs $sign2 .... $s win!\nPlay again?"
    }
  }
}

/**
 * The context required by the [[Application]]. It collect all the instances you have to inject into the [[Application]]
 */
trait ApplicationContext {
  val game : Game
}

/**
 * The Application coordinate the hands of a [[Game]] played by the [[User]].
 *
 * Until the User type the command 'q', the application play the [[Game]] either (Human)[[Player]] vs [[Computer]] or [[Computer]] vs [[Computer]]
 */
trait Application {
  self: ApplicationContext =>

  import Application._

  if (game.signs.size < 3) throw new InvalidGameException(s"Game '${game.getClass.getSimpleName}' should support at least 3 signs")

  implicit val strategy = new RandomMoveStrategy(game.signs)
  val commandParser = CommandParser(game.signs)

  @tailrec final def run() : Unit = {
    println(s"Choose between the following options and press enter:\n ${commandParser.available().map(e=>s"[${e._1}]${e._2}").toSeq.sorted.mkString(", ")}\n")

    val option = scala.io.StdIn.readLine("prompt>")

    run(option) match {
      case false =>
      case true => run()
    }
  }

  /**
   * Run hands until pass 'q'. It accepts command 'c' in order to play [[Computer]] vs [[Computer]] game or any other command in order to choose the correspondent [[Sign]] to play in the next hanf
   *
   * A not recognized command ask to the user to retry choosing a different command
   */
  def run(option: String): Boolean =
    commandParser.parse(option) match {
      case Quit =>
        println(s"\nBye Bye!")
        false

      case Invalid(e) =>
        println(s"$e. Please retry...")
        true

      case MoveComputer =>
        println(evaluate(Users.Computer, Users.Computer, game))
        true

      case MovePlayer(sign) =>
        println(evaluate(Users.Player(sign), Users.Computer, game))
        true
    }
}