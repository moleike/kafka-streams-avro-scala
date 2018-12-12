package com.moleike.kafka.streams.avro.generic

import org.apache.avro.generic.GenericRecord
import org.apache.kafka.common.serialization.Deserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import com.sksamuel.avro4s._
import java.util.{ Map => JMap }
import scala.collection.JavaConverters._

private[generic] class KafkaAvroDeserializerS[A: Encoder : Decoder : SchemaFor]
  (val inner: KafkaAvroDeserializer,
   val config: Map[String, Any]) extends Deserializer[A] {

  inner.configure(config.asJava, false)

  private val recordFormat = RecordFormat[A]

  override def configure(configs: JMap[String, _], isKey: Boolean): Unit = ()

  override def deserialize(topic: String, bytes: Array[Byte]): A =
    recordFormat.from(inner.deserialize(topic, bytes).asInstanceOf[GenericRecord])

  override def close(): Unit = inner.close()
}
