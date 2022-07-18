package com.piotr.stock.streaming.route.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class KafkaComponentUriBuilderTest {

  @Test
  void shouldCreateKafkaComponentUri() {
    //given
    String topic = "myStockTopic";
    String server = "localhost:9092";
    String zookeeper = "localhost:2181";
    String serializer = "kafka.serializer.StringEncoder";
    String expectedKafkaUri = "kafka:myStockTopic?brokers=localhost:9092&zookeeperHost=localhost:2181&serializerClass=kafka.serializer.StringEncoder";

    //when
    String actualKafkaUri = new KafkaComponentUriBuilder(topic)
        .withBroker(server)
        .withZookeeperHost(zookeeper)
        .withSerializerClass(serializer)
        .build();

    //then
    assertThat(actualKafkaUri).isEqualTo(expectedKafkaUri);
  }
}