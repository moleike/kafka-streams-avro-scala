# kafka-streams-avro-scala

This library provides marshalling of [Avro][avro] records into case classes with [Schema Registry][registry] for schema evolution.

[avro]: https://avro.apache.org/docs/current/
[registry]: https://docs.confluent.io/current/schema-registry/docs/index.html

## Install

To use this library add the following line to your dependencies:

```sbt
"io.github.moleike" %% "kafka-streams-avro-scala" % "0.2.5"
```

## Synopsis

First bring the implicit generic `SerDe` into scope:
```scala
import com.moleike.kafka.streams.avro.generic.Serdes._
```
Then configure the Serdes:
```scala
implicit val conf: Config = Map("schema.registry.url" -> "http://localhost:8081")
```
Use case classes with no boilerplate in your Kafka Streams application:
```scala
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream._

case class Store(name: String, address: String)

val builder = new StreamsBuilder()

val stores: KStream[String, Store] = builder.stream("stores")
```

## License

Licensed under the **[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)** (the "License");
you may not use this software except in compliance with the License.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

