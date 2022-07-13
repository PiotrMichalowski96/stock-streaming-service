package com.piotr.stock.streaming.rest.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.repository.StockProducer;
import com.piotr.stock.streaming.rest.model.Stock;
import com.piotr.stock.streaming.util.StockBuilder;
import java.math.BigDecimal;
import java.math.BigInteger;
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

  @Autowired
  private StockProducer stockProducer;

  @Test
  void shouldGetStockByQueryParams() {
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

  @Test
  void shouldGetStocksLimited() {
    //given
    int limitSize = 10;

    //when
    ResponseEntity<List<Stock>> response = stockApiDelegate.stock(null, null, null);
    HttpStatus status = response.getStatusCode();
    List<Stock> stocks = response.getBody();

    //then
    assertThat(status).isEqualTo(HttpStatus.OK);
    assertThat(stocks).hasSize(limitSize);
  }

  @Test
  void shouldGetNoContent() {
    //given
    String wrongTicker = "AABBCC";

    //when
    ResponseEntity<List<Stock>> response = stockApiDelegate.stock(wrongTicker, null, null);
    HttpStatus status = response.getStatusCode();

    //then
    assertThat(status).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void shouldSaveStock() {
    //given
    LocalDateTime localStockTimestamp = LocalDateTime.of(2022, 2, 11, 20, 52, 48);
    ZoneId zone = ZoneId.of("Europe/Warsaw");
    OffsetDateTime offsetStockTimestamp = localStockTimestamp.atZone(zone).toOffsetDateTime();

    Stock stockToSave = new StockBuilder()
        .withTicker("BTC/EUR")
        .withQuoteType("Digital Currency")
        .withStockMarket("Binance")
        .withPrice(new BigDecimal("19695.0000"))
        .withCurrency("EUR")
        .withVolume(66110L)
        .withStockTimestamp(offsetStockTimestamp)
        .build();

    StockEntity expectedSavedStock = StockEntity.builder()
        .id(12L)
        .ticker(stockToSave.getTicker())
        .stockType(stockToSave.getQuoteType())
        .exchange(stockToSave.getStockMarket())
        .price(stockToSave.getPrice())
        .currency(stockToSave.getCurrency())
        .volume(BigInteger.valueOf(stockToSave.getVolume()))
        .stockTimestamp(localStockTimestamp)
        .build();

    //when
    ResponseEntity<Stock> stockResponse = stockApiDelegate.addStock(stockToSave);
    StockEntity actualSavedStock = stockProducer.findById(expectedSavedStock.getId()).get();

    //then
    assertThat(stockResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(stockResponse.getBody()).usingRecursiveComparison().isEqualTo(stockToSave);
    assertThat(actualSavedStock).usingRecursiveComparison().isEqualTo(expectedSavedStock);
  }
}