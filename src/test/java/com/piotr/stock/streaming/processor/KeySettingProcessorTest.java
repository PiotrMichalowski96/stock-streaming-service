package com.piotr.stock.streaming.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.util.ExchangeUtil;
import org.apache.camel.component.kafka.KafkaConstants;
import org.junit.jupiter.api.Test;

class KeySettingProcessorTest {

  private final KeySettingProcessor keySettingProcessor = new KeySettingProcessor();

  @Test
  void shouldSetKey() {
    //given
    var id = -123L;
    var expectedKey = Math.abs(id);
    var stockEntity = StockEntity.builder()
        .id(id)
        .build();

    var exchange = ExchangeUtil.createExchange(stockEntity);

    //when
    keySettingProcessor.process(exchange);
    Long actualKey = exchange.getIn().getHeader(KafkaConstants.KEY, Long.class);

    //then
    assertThat(actualKey).isEqualTo(expectedKey);
  }

  @Test
  void shouldThrowExceptionBecauseOfMissingId() {
    //given
    var stockEntity = StockEntity.builder().build();
    var exchange = ExchangeUtil.createExchange(stockEntity);

    //whenThen
    assertThatThrownBy(() -> keySettingProcessor.process(exchange))
        .isInstanceOf(RuntimeException.class);
  }
}