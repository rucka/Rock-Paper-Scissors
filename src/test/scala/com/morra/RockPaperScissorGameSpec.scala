package com.morra

import RockPaperScissorsGame.{Paper, Rock, Scissors}
import org.scalacheck.Gen

import scala.util.{Failure, Try}

class RockPaperScissorGameSpec extends SpecBase("RockPaperScissorGame") {
  it should "returns all supported signs" in {
    Games.RockPaperScissors().signs should contain allOf(Paper, Rock, Scissors)
  }

  it should "returns Paper when evaluate Rock vs Paper" in {
    Games.RockPaperScissors().play(Rock, Paper) shouldBe Win(Paper)
  }

  it should "returns Rock when play evaluate vs Scissors" in {
    Games.RockPaperScissors().play(Rock, Scissors) shouldBe Win(Rock)
  }

  it should "returns Scissors when evaluate Paper vs Scissors" in {
    Games.RockPaperScissors().play(Paper, Scissors) shouldBe Win(Scissors)
  }

  it should "returns None when play evaluate vs Paper" in {
    Games.RockPaperScissors().play(Paper, Paper) shouldBe Draw
  }

  it should "returns None when play evaluate vs Rock" in {
    Games.RockPaperScissors().play(Rock, Rock) shouldBe Draw
  }

  it should "returns None when evaluate Scissors vs Scissors" in {
    Games.RockPaperScissors().play(Scissors, Scissors) shouldBe Draw
  }
  
  val validSignGen = Gen.oneOf[Sign](Games.RockPaperScissors().signs.toSeq)
  val invalidSignGen = Gen.oneOf[Sign](new FakeGame().signs.toSeq)

  it should "returns Draw when evaluate the same sign" in {
    forAll (validSignGen, validSignGen) {
      (s1, s2) => whenever(s1 == s2) {
        val r = Games.RockPaperScissors().play(s1, s2)
        r shouldBe Draw
      }
    }
  }

  it should "returns a Win when evaluate different signs" in {
    forAll (validSignGen, validSignGen) {
      (s1, s2) => whenever(s1 != s2) {
        val r = Games.RockPaperScissors().play(s1, s2)
        r shouldBe a[Win[_]]
      }
    }
  }

  it should "returns the same results both s1 vs s2 and s2 vs s1" in {
    forAll (validSignGen, validSignGen) {
      (s1, s2) => {
        val game = Games.RockPaperScissors()
        game.play(s1, s2) shouldBe game.play(s2, s1)
      }
    }
  }

  it should "fails when evaluate both invalid signs" in {
    val engine = new RockPaperScissorsGame()
    forAll (invalidSignGen, invalidSignGen) {
      (s1, s2) => {
        val engine = new RockPaperScissorsGame()
        Try(engine.play(s1, s2)) shouldBe a[Failure[_]]
      }
    }
  }

  it should "fails when evaluate one invalid signs" in {
    val engine = new RockPaperScissorsGame()
    forAll (invalidSignGen, validSignGen) {
      (s1, s2) => {
        val engine = new RockPaperScissorsGame()
        Try(engine.play(s1, s2)) shouldBe a[Failure[_]]
      }
    }
  }
}