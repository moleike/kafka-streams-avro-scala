package com.moleike.kafka.streams.avro.generic

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient

import org.apache.kafka.common.serialization.{ Deserializer, Serde, Serdes => JSerdes, Serializer }
import io.confluent.kafka.serializers.{ KafkaAvroSerializer, KafkaAvroDeserializer }
import com.sksamuel.avro4s._

import org.scalacheck.{Arbitrary, Gen, Prop}
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers

case class User(name: String)

trait ArbitraryInstances {

  implicit def arbitraryUser: Arbitrary[User] =
    Arbitrary(Arbitrary.arbitrary[String].map(User.apply(_)))
}

class SerdesTest extends FunSuite with ArbitraryInstances {

  val ANY_TOPIC = "any-topic"
  val schemaRegistryClient = new MockSchemaRegistryClient()

  val serializer = new KafkaAvroSerializer(schemaRegistryClient)
  val deserializer = new KafkaAvroDeserializer(schemaRegistryClient)
  val serdeConfig = Map("schema.registry.url" -> "fake")

  def serde[A: Encoder: Decoder: SchemaFor](): Serde[A] =
    JSerdes.serdeFrom(
      new KafkaAvroSerializerS(serializer, serdeConfig),
      new KafkaAvroDeserializerS(deserializer, serdeConfig)
    )

  val userSerde: Serde[User] = serde()

  test("should round-trip") {

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
  }

}
