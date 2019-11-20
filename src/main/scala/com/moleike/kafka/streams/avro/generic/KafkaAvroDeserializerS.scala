package com.moleike.kafka.streams.avro.generic

import org.apache.avro.generic.IndexedRecord
import org.apache.kafka.common.serialization.Deserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import com.sksamuel.avro4s._
import java.util.{ Map => JMap }

import scala.collection.JavaConverters._

class KafkaAvroDeserializerS[A: Encoder : Decoder : SchemaFor]
  (val config: Map[String, Any],
   val inner: KafkaAvroDeserializer = new KafkaAvroDeserializer()
  ) extends Deserializer[A] {

  inner.configure(config.asJava, false)

  private val recordFormat = RecordFormat[A]

  override def configure(configs: JMap[String, _], isKey: Boolean): Unit = ()

  override def deserialize(topic: String, bytes: Array[Byte]): A =
    if (bytes == null) null.asInstanceOf[A]
    else recordFormat.from(inner.deserialize(topic, bytes).asInstanceOf[IndexedRecord])

  override def close(): Unit = inner.close()
}
