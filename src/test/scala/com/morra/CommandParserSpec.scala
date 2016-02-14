package com.morra

import com.morra.FakeGame.Fake1
import FakeGame.Fake1
import org.scalacheck.Gen

class CommandParserSpec extends SpecBase("CommandParser") {
  import CommandParser._
  val signs = new FakeGame().signs

  it should "throws exception when constructed with null map" in {
    a [CommandSignMapNullOrEmptyException] shouldBe thrownBy (new CommandParser(null))
  }

  it should "throws exception when constructed with empty map" in {
    a [CommandSignMapNullOrEmptyException] shouldBe thrownBy (new CommandParser(Map.empty))
  }

  it should "returns available options" in {
    CommandParser(signs).available() should contain allOf("q" -> "Quit", "c" -> "Move Computer", "1" -> "Fake1", "2" -> "Fake2", "3" -> "Fake3")
  }

  it should "returns Quit if option is \"q\"" in {
    CommandParser(signs).parse("q") shouldBe Quit
  }

  it should "does not care case" in {
    CommandParser(signs).parse("Q") shouldBe Quit
  }

  it should "returns MoveCompute if option is \"c\"" in {
    CommandParser(signs).parse("c") shouldBe MoveComputer
  }

  it should "returns Sign if option corresponds to a valid sign" in {
    CommandParser(signs).parse("1") shouldBe MovePlayer(Fake1)
  }

  val allSigns = Games.RockPaperScissors().signs ++ new FakeGame().signs
  val generator = for {
    v <- Gen.choose(1, allSigns.size)
  } yield util.Random.shuffle(allSigns).take(v)

  it should "always returns 'MovePlayer' when option is a number between 1 and the number of signs" in {
    forAll(generator, Gen.choose(1, allSigns.size)) {
      (signs, option)  => whenever(signs.nonEmpty && option > 0 && option <= signs.size) {
        CommandParser(signs).parse(option.toString) shouldBe a [MovePlayer[_]]
      }
    }
  }

  it should "always returns 'InvalidCommand' foreach options different than q, c and a number between 1 and the number of signs" in {
    def optionsOf(signs: Set[Sign]) = Range(1, signs.size + 1).map(_.toString)

    def optionsIsACommand(signs: Set[Sign], option: Char) = {
      option.toString.toLowerCase match {
        case "q" | "c" => true
        case o if optionsOf(signs).contains(o) => true
        case _ => false
      }
    }

    forAll(generator, Gen.alphaNumChar) {
      (signs, option)  => whenever(signs.nonEmpty && !optionsIsACommand(signs, option)) {
        CommandParser(signs).parse(option.toString) shouldBe a [Invalid]
      }
    }
  }

  it should "always make 'Quit' command available for each signs sequence" in {
    forAll(generator) {
      signs  => whenever(signs.nonEmpty) {
        CommandParser(signs).parse("q") shouldBe Quit
      }
    }
  }

  it should "always make 'MoveCommand' command available for each signs sequence" in {
    forAll(generator) {
      signs  => whenever(signs.nonEmpty) {
        CommandParser(signs).parse("c") shouldBe MoveComputer
      }
    }
  }
}
