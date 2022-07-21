package com.piotr.stock.streaming.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.piotr.stock.streaming.entity.StockEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class StockSerializer implements Serializer<StockEntity> {

  private final ObjectMapper objectMapper;

  public StockSerializer() {
    objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Override
  public byte[] serialize(String topic, StockEntity stock) {
    if (stock == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsBytes(stock);
    } catch (JsonProcessingException e) {
      logger.error("Error serializing Stock: {}", stock);
      throw new SerializationException("Error serializing JSON message", e);
    }
  }
}
