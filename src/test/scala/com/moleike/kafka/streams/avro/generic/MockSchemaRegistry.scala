package com.moleike.kafka.streams.avro.generic

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient
import org.apache.kafka.common.serialization.{ Deserializer, Serializer }
import io.confluent.kafka.serializers.{ KafkaAvroSerializer, KafkaAvroDeserializer }
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig._
import com.sksamuel.avro4s.{ Encoder, Decoder, SchemaFor }

trait MockSchemaRegistry {

  private val client = new MockSchemaRegistryClient()
  private val serdeConfig: Serdes.Config = Map(SCHEMA_REGISTRY_URL_CONFIG -> "fake")

  implicit def serializerMock[A: Encoder: Decoder: SchemaFor]: Serializer[A] =
    new KafkaAvroSerializerS(serdeConfig, new KafkaAvroSerializer(client))

  implicit def deserializerMock[A: Encoder: Decoder: SchemaFor]: Deserializer[A] =
    new KafkaAvroDeserializerS(serdeConfig, new KafkaAvroDeserializer(client))
}
