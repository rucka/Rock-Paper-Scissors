package com.morra

import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{TryValues, BeforeAndAfterAll, FlatSpecLike, Matchers}

abstract class SpecBase(description: String) extends FlatSpecLike with Matchers with BeforeAndAfterAll with TryValues  with GeneratorDrivenPropertyChecks {
  behavior of description
}
