package com.moleike.kafka.streams.avro.generic

import org.apache.kafka.common.serialization.{ Deserializer, Serde, Serdes => JSerdes, Serializer }
import io.confluent.kafka.serializers.{ KafkaAvroSerializer, KafkaAvroDeserializer }
import com.sksamuel.avro4s._

object Serdes {

  type Config = Map[String, Any]

  implicit def serializer[A: Encoder: Decoder: SchemaFor]
    (implicit config: Config): Serializer[A] =
    new KafkaAvroSerializerS(new KafkaAvroSerializer(), config)

  implicit def deserializer[A: Encoder: Decoder: SchemaFor]
    (implicit config: Config): Deserializer[A] =
    new KafkaAvroDeserializerS(new KafkaAvroDeserializer(), config)

  implicit def serde[A: Encoder: Decoder: SchemaFor]
    (implicit config: Config): Serde[A] =
    JSerdes.serdeFrom(serializer, deserializer)
}
