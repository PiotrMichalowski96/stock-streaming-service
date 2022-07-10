package com.piotr.stock.streaming.rest.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.piotr.stock.streaming.rest.model.Stock;
import com.piotr.stock.streaming.util.StockBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("TEST")
class StockApiDelegateIntTest {

  @Autowired
  private StockApiDelegate stockApiDelegate;

  @Test
  void shouldRetrieveStocks() {
    //given
    ZoneId zone = ZoneId.of("Europe/Warsaw");
    OffsetDateTime stockTimestamp = LocalDateTime.of(2022, 2, 11, 20, 52, 48)
        .atZone(zone).toOffsetDateTime();

    Stock expectedStock = new StockBuilder()
        .withTicker("BTC/USD")
        .withQuoteType("Digital Currency")
        .withStockMarket("Binance")
        .withPrice(new BigDecimal("43993.0000"))
        .withCurrency("US Dollar")
        .withVolume(72110L)
        .withStockTimestamp(stockTimestamp)
        .build();

    String ticker = "BTC/USD";

    //when
    ResponseEntity<List<Stock>> response = stockApiDelegate.stock(ticker, null, null);
    HttpStatus status = response.getStatusCode();
    List<Stock> stocks = response.getBody();

    //then
    assertThat(status).isEqualTo(HttpStatus.OK);
    assertThat(stocks).hasSize(1);
    assertThat(stocks.get(0)).usingRecursiveComparison().isEqualTo(expectedStock);
  }
}