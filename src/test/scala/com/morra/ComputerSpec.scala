package com.morra

import FakeGame.Fake1
import org.scalacheck.Gen

class ComputerSpec extends SpecBase("Computer") {
  var fake1Strategy = new MoveStrategy {
    override def next(): Sign = Fake1
  }

  it should "moves depending on his strategy" in {
    implicit val strategy = fake1Strategy
    var computer = new Computer()

    forAll(Gen.choose(0, 100)) { _ =>
      computer.move() shouldBe Fake1
    }
  }
}

class RandomMoveStrategySpec extends SpecBase("RandomMoveStrategy") {

  it should "generate random signs" in {
    val signs = new FakeGame().signs
    val strategy = new RandomMoveStrategy(signs)

    var moves : Set[Sign] = Set.empty
    forAll (Gen.choose(0, 100)) {
      _ => {
        moves = moves + strategy.next()
      }
    }
    moves shouldBe signs
  }
}