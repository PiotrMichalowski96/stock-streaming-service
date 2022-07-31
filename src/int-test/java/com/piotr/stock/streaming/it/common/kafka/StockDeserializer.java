package com.piotr.stock.streaming.it.common.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;

@RequiredArgsConstructor
public class StockDeserializer<T> implements Deserializer<T> {

  private final Class<T> clazz;

  @Override
  public T deserialize(String topic, byte[] data) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    try {
      return mapper.readValue(data, clazz);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
