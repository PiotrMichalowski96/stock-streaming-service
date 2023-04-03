package com.piotr.stock.streaming.route.common;

import static com.piotr.stock.streaming.util.ExchangeUtil.createExchange;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import org.junit.jupiter.api.Test;

class RouteUtilTest {

  @Test
  void shouldWrapProcessing() throws Exception {
    //given
    Function<String, Long> textToNumber = Long::valueOf;
    var exchange = createExchange("1996");
    var expectedNumber = 1996;

    //when
    RouteUtil.wrapProcessing(textToNumber).process(exchange);
    Long actualNumber = exchange.getIn().getBody(Long.class);

    //then
    assertThat(actualNumber).isEqualTo(expectedNumber);
  }
}