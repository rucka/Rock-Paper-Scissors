package com.morra

import FakeGame.{Fake1, Fake3, Fake2}


object FakeGame {
  case object Fake1 extends Sign
  case object Fake2 extends Sign
  case object Fake3 extends Sign
}

class FakeGame extends Game {
  override def play = {
    case Hand(Fake1, Fake2) => Win(Fake1)
    case Hand(Fake2, Fake1) => Win(Fake1)
    case _ => Draw
  }

  override def signs: Set[Sign] = Set(Fake1, Fake2, Fake3)
}