package com.moleike.kafka.streams.avro.generic

import org.apache.kafka.common.serialization.Serializer
import io.confluent.kafka.serializers.KafkaAvroSerializer
import com.sksamuel.avro4s._
import java.util.{ Map => JMap }
import scala.collection.JavaConverters._

class KafkaAvroSerializerS[A: Encoder : Decoder : SchemaFor]
  (val config: Map[String, Any],
   val inner: KafkaAvroSerializer = new KafkaAvroSerializer()
  ) extends Serializer[A] {

  inner.configure(config.asJava, false)

  private val recordFormat = RecordFormat[A]

  override def configure(configs: JMap[String, _], isKey: Boolean): Unit = ()

  override def serialize(topic: String, value: A): Array[Byte] =
    if (value == null) null
    else inner.serialize(topic, recordFormat.to(value))

  override def close(): Unit = inner.close()
}



