package com.piotr.stock.streaming.it.common.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongDeserializer;

public class MessageConsumer<T> {

  private final KafkaConsumer<Long, T> kafkaConsumer;

  public MessageConsumer(String server, Deserializer<T> payloadDeserializer, Collection<String> topics) {
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
    final Deserializer<Long> longDeserializer = new LongDeserializer();
    kafkaConsumer = new KafkaConsumer<>(properties, longDeserializer, payloadDeserializer);
    kafkaConsumer.subscribe(topics);
  }

  public List<T> readRecords() {
    List<T> recordList = new ArrayList<>();
    ConsumerRecords<Long, T> records = kafkaConsumer.poll(Duration.ofMillis(100));
    records.forEach(record -> recordList.add(record.value()));
    return recordList;
  }

  public void close() {
    kafkaConsumer.close();
  }
}
