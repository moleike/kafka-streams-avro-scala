package com.moleike.kafka.streams.avro

import org.apache.avro.generic.GenericRecord
import org.apache.kafka.common.serialization.{ Deserializer, Serde, Serdes, Serializer }
import io.confluent.kafka.serializers.{ KafkaAvroSerializer, KafkaAvroDeserializer }
import com.sksamuel.avro4s._
import java.util.{ Map => JMap }

package object generic {

  type SerDesConfig = Map[String, Any]

  import scala.collection.JavaConverters._

  implicit def serializer[A: Encoder: Decoder: SchemaFor]
    (implicit config: SerDesConfig): Serializer[A] = new Serializer[A] {

      private val inner = new KafkaAvroSerializer()

      inner.configure(config.asJava, false)

      override def configure(configs: JMap[String, _], isKey: Boolean): Unit = ()

      override def serialize(topic: String, value: A): Array[Byte] =
        inner.serialize(topic, RecordFormat[A].to(value))

      override def close(): Unit = inner.close()
    }

  implicit def deserializer[A: Encoder: Decoder: SchemaFor]
    (implicit config: SerDesConfig): Deserializer[A] = new Deserializer[A] {

      private val inner = new KafkaAvroDeserializer()

      inner.configure(config.asJava, false)

      override def configure(configs: JMap[String, _], isKey: Boolean): Unit = ()

      override def deserialize(topic: String, bytes: Array[Byte]): A =
        RecordFormat[A].from(inner.deserialize(topic, bytes).asInstanceOf[GenericRecord])

      override def close(): Unit = inner.close()
    }

  implicit def serde[A: Encoder: Decoder: SchemaFor](implicit config: SerDesConfig): Serde[A] =
    Serdes.serdeFrom(serializer, deserializer)
}
