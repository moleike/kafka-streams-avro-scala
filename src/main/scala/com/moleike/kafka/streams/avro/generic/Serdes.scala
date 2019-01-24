package com.moleike.kafka.streams.avro.generic

import org.apache.kafka.common.serialization.{ Deserializer, Serde, Serdes => JSerdes, Serializer }

import com.sksamuel.avro4s._

object Serdes {

  type Config = Map[String, Any]

  implicit def serializer[A <: Product : Encoder: Decoder: SchemaFor]
    (implicit config: Config): Serializer[A] =
    new KafkaAvroSerializerS(config)

  implicit def deserializer[A <: Product : Encoder: Decoder: SchemaFor]
    (implicit config: Config): Deserializer[A] =
    new KafkaAvroDeserializerS(config)

  implicit def serde[A <: Product : Encoder: Decoder: SchemaFor]
    (implicit serializer: Serializer[A], deserializer: Deserializer[A]): Serde[A] =
    JSerdes.serdeFrom(serializer, deserializer)
}
