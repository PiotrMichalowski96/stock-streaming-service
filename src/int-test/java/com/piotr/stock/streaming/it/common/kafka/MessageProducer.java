package com.piotr.stock.streaming.it.common.kafka;

import java.util.Properties;
import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;

public class MessageProducer<T> {

  private final KafkaProducer<Long, T> kafkaProducer;

  public MessageProducer(String server, Serializer<T> payloadSerializer) {
    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
    properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
    properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
    properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
    final Serializer<Long> longSerializer = new LongSerializer();
    kafkaProducer = new KafkaProducer<>(properties, longSerializer, payloadSerializer);
  }

  public void send(String topic, T payload) {
    Long key = RandomUtils.nextLong();
    ProducerRecord<Long, T> record = new ProducerRecord<>(topic, key, payload);
    kafkaProducer.send(record);
  }

  public void close() {
    kafkaProducer.close();
  }
}
