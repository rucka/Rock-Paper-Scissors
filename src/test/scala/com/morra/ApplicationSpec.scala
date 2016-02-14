package com.morra

import Application.InvalidGameException
import FakeGame.{Fake1, Fake2}
import org.scalacheck.Gen

object ApplicationSpec {
  def fakeApp() = new Application with ApplicationContext {
    lazy override val game: Game = new FakeGame()
  }

  def invalidApp() = new Application with ApplicationContext {
    lazy override val game: Game = new Game {
      override def play: Evaluate = ???
      override def signs: Set[Sign] = Set(Fake1, Fake2)
    }
  }

  def constantStrategy(sign: Sign) = new MoveStrategy {
    override def next(): Sign = sign
  }
}

class ApplicationSpec extends SpecBase("Application") {
  import ApplicationSpec._

  it should "requires Game with at least three Signs" in {
    a[InvalidGameException] shouldBe thrownBy(invalidApp())
  }

  it should "returns false, only choosing quit command" in {
    val app = fakeApp()

    val generator = for {
      v1 <- Gen.alphaStr
      v2 <- Gen.oneOf(Range(0, 10).map(_.toString).toSeq)
      v3 <- Gen.oneOf(app.commandParser.available().keys.toSeq)
      v4 <- Gen.oneOf(v1, v2, v3)
    } yield v4

    forAll (generator) { o =>
      val again = app.run(o)
      assert(again || (!again && o == "q"))
    }
  }

  it should "returns true choosing invalid command" in {
    val app = fakeApp()
    app.run("invalid option") shouldBe true
  }

  it should "returns true choosing 'move computer' command" in {
    val app = fakeApp()
    app.run("c") shouldBe true
  }

  it should "returns true choosing 'move player' command" in {
    val app = fakeApp()
    app.run("1") shouldBe true
  }

  it should "evaluates game Draw computer vs computer result" in {
    val result = Application.evaluate(Users.Computer(constantStrategy(Fake1)), Users.Computer(constantStrategy(Fake1)), new FakeGame())
    result shouldBe "Playing Fake1 vs Fake1 .... Draw!\nPlay again?"
  }

  it should "evaluates game Draw player vs computer result" in {
    val result = Application.evaluate(Users.Player(Fake1), Users.Computer(constantStrategy(Fake1)), new FakeGame())
    result shouldBe "Playing Fake1 vs Fake1 .... You Draw!\nPlay again?"
  }

  it should "evaluates game Win player1 result" in {
    val result = Application.evaluate(Users.Player(Fake1), Users.Computer(constantStrategy(Fake2)), new FakeGame())
    result shouldBe "Playing Fake1 vs Fake2 .... You Win!\nPlay again?"
  }

  it should "evaluates game Lost player1 result" in {
    val result = Application.evaluate(Users.Player(Fake2), Users.Computer(constantStrategy(Fake1)), new FakeGame())
    result shouldBe "Playing Fake2 vs Fake1 .... You Lost:(\nPlay again?"
  }

  it should "evaluates game Win computer vs computer result" in {
    val result = Application.evaluate(Users.Computer(constantStrategy(Fake1)), Users.Computer(constantStrategy(Fake2)), new FakeGame())
    result shouldBe "Playing Fake1 vs Fake2 .... Fake1 win!\nPlay again?"
  }
}