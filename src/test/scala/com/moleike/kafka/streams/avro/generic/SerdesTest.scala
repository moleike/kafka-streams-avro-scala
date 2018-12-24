package com.moleike.kafka.streams.avro.generic

import org.scalacheck.{Arbitrary, Gen, Prop}
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers

case class User(name: String)

trait ArbitraryInstances {

  implicit def arbitraryUser: Arbitrary[User] =
    Arbitrary(Arbitrary.arbitrary[String].map(User.apply(_)))
}

class SerdesTest extends FunSuite with ArbitraryInstances with MockSchemaRegistry {

  import Serdes._

  val ANY_TOPIC = "any-topic"

  test("should round-trip") {

    val userSerde = serde[User]

    Checkers.check(
      Prop.forAll { (user: User) =>

        val roundtrippedUser = userSerde.deserializer()
            .deserialize(
              ANY_TOPIC,
              userSerde.serializer()
                .serialize(ANY_TOPIC, user)
            )

        user  === roundtrippedUser
      }
    )

    userSerde.close()
  }
}
