package com.morra

import CommandParser.CommandSignMapNullOrEmptyException

/**
 *  Factory for the [[CommandParser]] instances.
 *
 *  The companion object contains the [[CommandSignMapNullOrEmptyException]] declaration too
 */
object CommandParser {
  def apply(commandSignMap : Map[String, Sign]) = new CommandParser(commandSignMap)

  /**
   * Create a [[CommandParser]] assigning foreach sign a progressive number starting from 1.
   *
   * Es:
   *
   *   - 1 -> Rock
   *   - 2 -> Paper
   *   - 3 -> Scissors
   */
  def apply(signs : Set[Sign]) = {
    val map = Range(1, signs.size + 1).zip(signs).map(e => e._1.toString -> e._2).toMap
    new CommandParser(map)
  }

  /**
   * The CommandParser has been created without specify a valid map between the option (the value typed by the user) and the [[Command]]
   */
  case class CommandSignMapNullOrEmptyException() extends Exception
}

/**
 * Parses the input of the user and detects the correspondent [[Command]]. An [[Invalid]] [[Command]] returns in case of not recognized input [[Command]]
 */
class CommandParser(commandSignMap : Map[String, Sign]) {
  if (commandSignMap == null || commandSignMap == Map.empty) throw CommandSignMapNullOrEmptyException()
  def parse(option: String): Command[Sign] = option.toLowerCase match {
    case "q" => Quit
    case "c" => MoveComputer
    case o => commandSignMap
      .find(_._1 == o)
      .map(e => MovePlayer(e._2))
      .getOrElse(Invalid(s"command '$o' not recognized"))
  }

  /**
   * Returns the list of the available commands. Additional to the command passed to constructor, a [[CommandParser]] accepts 'q' for Quit from the application and the 'c' that play the next hand as [[Computer]] vs [[Computer]]
   */
  def available() : Map[String, String] = Map("q" -> "Quit", "c" -> "Move Computer") ++ commandSignMap.toSeq.sortBy(_._1).map(e => e._1 -> e._2.toString)
}

/**
 * A base class representing a Command choosed by the user
 */
sealed abstract class Command[+S <: Sign] {
  def isQuit: Boolean
  def isInError: Boolean = errorMessage.isDefined
  def errorMessage: Option[String] = None
  def getSign: S
}

/**
 * Represents a [[Sign]] moved by a [[Player]]
 */
final case class MovePlayer[+S <: Sign](x: S) extends Command[S] {
  def isQuit = false
  def getSign = x
}

/**
 * Represents an Invalid [[Command]]
 */
final case class Invalid(_errorMessage: String) extends Command[Nothing] {
  def isQuit = false
  def getSign = throw new NoSuchElementException("Invalid.get")
  override def errorMessage: Option[String] = Some(_errorMessage)
}

/**
 * Represents a request to play next hand as [[Computer]] vs [[Computer]]
 */
case object MoveComputer extends Command[Nothing] {
  def isQuit = false
  def getSign = throw new NoSuchElementException("Quit.get")
}

/**
 * Represents a request to quit the application
 */
case object Quit extends Command[Nothing] {
  def isQuit = true
  def getSign = throw new NoSuchElementException("Quit.get")
}