package com.piotr.stock.streaming.route.common;

public class KafkaComponentUriBuilder {

  private static final String KAFKA_COMPONENT_URI_PREFIX = "kafka";
  private static final String BROKER_PARAM = "brokers";
  private static final String ZOOKEEPER_PARAM = "zookeeperHost";
  private static final String VALUE_SERIALIZER_PARAM = "valueSerializer";
  private static final String KEY_SERIALIZER_PARAM = "keySerializer";

  //Below constants for Kafka transactional producer in Camel (https://camel.apache.org/components/3.17.x/kafka-component.html#_kafka_transaction)
  private static final String TRANSACTIONAL_PARAM = "additional-properties[transactional.id]";
  private static final String TRANSACTIONAL_VALUE = "1234";
  private static final String IDEMPOTENCE_PARAM = "additional-properties[enable.idempotence]";
  private static final String IDEMPOTENCE_VALUE = "true";
  private static final String RETRIES_PARAM = "additional-properties[retries]";
  private static final String RETRIES_VALUE = "5";
  private static final String ACKS_PARAM = "requestRequiredAcks";
  private static final String ACKS_VALUE = "all";

  private final StringBuilder kafkaUriBuilder;
  private boolean optionParamPresent;

  public KafkaComponentUriBuilder(String topic) {
    optionParamPresent = false;
    kafkaUriBuilder = new StringBuilder()
        .append(KAFKA_COMPONENT_URI_PREFIX)
        .append(":")
        .append(topic);
  }

  public KafkaComponentUriBuilder withBroker(String broker) {
    addParameterInUri(BROKER_PARAM, broker);
    return this;
  }

  public KafkaComponentUriBuilder withZookeeperHost(String zookeeperHost) {
    addParameterInUri(ZOOKEEPER_PARAM, zookeeperHost);
    return this;
  }

  public KafkaComponentUriBuilder withValueSerializer(String serializerClass) {
    addParameterInUri(VALUE_SERIALIZER_PARAM, serializerClass);
    return this;
  }

  public KafkaComponentUriBuilder withKeySerializer(String serializerClass) {
    addParameterInUri(KEY_SERIALIZER_PARAM, serializerClass);
    return this;
  }

  public KafkaComponentUriBuilder withTransactionSupport() {
    addParameterInUri(TRANSACTIONAL_PARAM, TRANSACTIONAL_VALUE);
    addParameterInUri(IDEMPOTENCE_PARAM, IDEMPOTENCE_VALUE);
    addParameterInUri(RETRIES_PARAM, RETRIES_VALUE);
    addParameterInUri(ACKS_PARAM, ACKS_VALUE);
    return this;
  }

  public String build() {
    return kafkaUriBuilder.toString();
  }

  private void addParameterInUri(String paramName, String paramValue) {
    if (optionParamPresent) {
      kafkaUriBuilder.append("&");
    } else {
      optionParamPresent = true;
      kafkaUriBuilder.append("?");
    }
    kafkaUriBuilder
        .append(paramName)
        .append("=")
        .append(paramValue);
  }
}
