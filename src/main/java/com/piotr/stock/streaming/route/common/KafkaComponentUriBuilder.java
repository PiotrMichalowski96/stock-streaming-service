package com.piotr.stock.streaming.route.common;

public class KafkaComponentUriBuilder {

  private static final String KAFKA_COMPONENT_URI_PREFIX = "kafka";
  private static final String BROKER_PARAM = "brokers";
  private static final String ZOOKEEPER_PARAM = "zookeeperHost";
  private static final String SERIALIZER_PARAM = "serializerClass";

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

  public KafkaComponentUriBuilder withSerializerClass(String serializerClass) {
    addParameterInUri(SERIALIZER_PARAM, serializerClass);
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
