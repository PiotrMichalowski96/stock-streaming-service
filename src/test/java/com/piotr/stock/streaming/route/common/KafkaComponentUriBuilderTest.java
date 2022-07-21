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
    String stringSerializer = "kafka.serializer.StringEncoder";
    String longSerializer = "kafka.serializer.LongEncoder";
    String expectedKafkaUri = "kafka:myStockTopic?brokers=localhost:9092"
        + "&zookeeperHost=localhost:2181"
        + "&valueSerializer=kafka.serializer.StringEncoder"
        + "&keySerializer=kafka.serializer.LongEncoder";

    //when
    String actualKafkaUri = new KafkaComponentUriBuilder(topic)
        .withBroker(server)
        .withZookeeperHost(zookeeper)
        .withValueSerializer(stringSerializer)
        .withKeySerializer(longSerializer)
        .build();

    //then
    assertThat(actualKafkaUri).isEqualTo(expectedKafkaUri);
  }
}