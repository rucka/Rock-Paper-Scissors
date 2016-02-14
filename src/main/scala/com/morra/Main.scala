package com.morra

/**
 * Entry point of the app. The Application injects the [[Game]] to play in the cake pattern way.
 *
 * If you want to support more than one Game in the same session, create a class extending the Application and pass the [[Game]] to its constructor
 */
object Main extends App with Application with ApplicationContext {
  lazy override val game: Game = Games.RockPaperScissors()

  println("Rock, Paper, Scissors: make your move!")
  run()
}