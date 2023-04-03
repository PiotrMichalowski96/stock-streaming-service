package com.piotr.stock.streaming.json;

import static org.assertj.core.api.Assertions.assertThat;

import com.piotr.stock.streaming.entity.StockEntity;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class StockSerializerTest {

  private final StockSerializer stockSerializer = new StockSerializer();

  @Test
  void shouldSerializeStock() {
    //given
    var topic = "topic";
    var stockEntity = StockEntity.builder()
        .id(123L)
        .currency("PLN")
        .price(BigDecimal.ONE)
        .build();

    //when
    byte[] bytes = stockSerializer.serialize(topic, stockEntity);

    //then
    assertThat(bytes).isNotEmpty();
  }

  @Test
  void shouldReturnNullInCaseOfNullStock() {
    //given
    var topic = "topic";

    //when
    byte[] bytes = stockSerializer.serialize(topic, null);

    //then
    assertThat(bytes).isNull();
  }
}