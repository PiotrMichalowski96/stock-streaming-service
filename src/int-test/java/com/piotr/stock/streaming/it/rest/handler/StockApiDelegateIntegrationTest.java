package com.piotr.stock.streaming.it.rest.handler;

import static com.piotr.stock.streaming.it.util.AwaitilityUtil.testAsynchronous;
import static org.assertj.core.api.Assertions.assertThat;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.it.common.KsqlDbIntegrationTest;
import com.piotr.stock.streaming.rest.handler.StockApiDelegate;
import com.piotr.stock.streaming.rest.model.Stock;
import com.piotr.stock.streaming.util.StockBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class StockApiDelegateIntegrationTest extends KsqlDbIntegrationTest {

  private final StockApiDelegate stockApiDelegate;

  @Autowired
  public StockApiDelegateIntegrationTest(StockApiDelegate stockApiDelegate,
      @Value("${kafka.server}") String bootstrap,
      @Value("${kafka.topic}") String topic) {

    super(bootstrap, topic);
    this.stockApiDelegate = stockApiDelegate;
  }


  @Test
  void shouldGetStockByQueryParams() {
    LocalDateTime stockTimestamp = LocalDateTime.of(2022, 2, 11, 20, 52, 48);
    ZoneId zone = ZoneId.of("Europe/Warsaw");
    OffsetDateTime offsetStockTimestamp = stockTimestamp.atZone(zone).toOffsetDateTime();

    StockEntity stock = StockEntity.builder()
        .id(1L)
        .ticker("BTC/USD")
        .stockType("Digital Currency")
        .exchange("Binance")
        .price(new BigDecimal("43993.0"))
        .currency("US Dollar")
        .volume(72110L)
        .stockTimestamp(stockTimestamp)
        .build();

    List<Stock> expectedStocks = List.of(new StockBuilder()
        .withTicker("BTC/USD")
        .withQuoteType("Digital Currency")
        .withStockMarket("Binance")
        .withPrice(new BigDecimal("43993.0"))
        .withCurrency("US Dollar")
        .withVolume(72110L)
        .withStockTimestamp(offsetStockTimestamp)
        .build());

    sendStock(stock);

    testAsynchronous(() -> {
      ResponseEntity<List<Stock>> response = stockApiDelegate.stock(stock.getTicker(), null, null);
      assertStocksResponse(response, expectedStocks);
    });
  }

  @Test
  void shouldGetStocksLimited() {
    int limitSize = 10;
    Stream.generate(() -> StockEntity.builder()
            .id(1L)
            .ticker(RandomStringUtils.randomAlphabetic(8))
            .stockType(RandomStringUtils.randomAlphabetic(8))
            .exchange(RandomStringUtils.randomAlphabetic(8))
            .price(new BigDecimal("43993.0"))
            .currency(RandomStringUtils.randomAlphabetic(8))
            .volume(72110L)
            .stockTimestamp(LocalDateTime.of(2022, 2, 11, 20, 52, 48))
            .build())
        .limit(limitSize)
        .forEach(super::sendStock);

    testAsynchronous(() -> {
      ResponseEntity<List<Stock>> response = stockApiDelegate.stock(null, null, null);
      List<Stock> stocks = response.getBody();
      assertResponseCode(response, HttpStatus.OK);
      assertThat(stocks).hasSize(limitSize);
    });
  }

  @Test
  void shouldGetNoContent() {
    String wrongTicker = "AABBCC";

    testAsynchronous(() -> {
      ResponseEntity<List<Stock>> response = stockApiDelegate.stock(wrongTicker, null, null);
      assertResponseCode(response, HttpStatus.NO_CONTENT);
    });
  }

  @Test
  void shouldSaveStock() {
    LocalDateTime localStockTimestamp = LocalDateTime.of(2022, 2, 11, 20, 52, 48);
    ZoneId zone = ZoneId.of("Europe/Warsaw");
    OffsetDateTime offsetStockTimestamp = localStockTimestamp.atZone(zone).toOffsetDateTime();

    Stock stockToSave = new StockBuilder()
        .withTicker("BTC/DKK")
        .withQuoteType("Digital Currency")
        .withStockMarket("Binance")
        .withPrice(new BigDecimal("165793.21"))
        .withCurrency("Danish krone")
        .withVolume(61230L)
        .withStockTimestamp(offsetStockTimestamp)
        .build();

    StockEntity expectedSavedStock = StockEntity.builder()
        .id((long) stockToSave.hashCode())
        .ticker(stockToSave.getTicker())
        .stockType(stockToSave.getQuoteType())
        .exchange(stockToSave.getStockMarket())
        .price(stockToSave.getPrice())
        .currency(stockToSave.getCurrency())
        .volume(stockToSave.getVolume())
        .stockTimestamp(localStockTimestamp)
        .build();

    ResponseEntity<Stock> stockResponse = stockApiDelegate.addStock(stockToSave);

    testAsynchronous(() -> {
      List<StockEntity> actualStocks = readStocks().stream()
          .filter(stock -> expectedSavedStock.getId().equals(stock.getId()))
          .toList();
      assertThat(actualStocks).hasSize(1);
      assertThat(actualStocks.get(0)).usingRecursiveComparison().isEqualTo(expectedSavedStock);
    });
    assertThat(stockResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(stockResponse.getBody()).usingRecursiveComparison().isEqualTo(stockToSave);
  }

  private void assertResponseCode(ResponseEntity<List<Stock>> stockResponse, HttpStatus httpStatus) {
    HttpStatus status = stockResponse.getStatusCode();
    assertThat(status).isEqualTo(httpStatus);
  }

  private void assertStocksResponse(ResponseEntity<List<Stock>> stockResponse,
      List<Stock> expectedStocks) {
    List<Stock> stocks = stockResponse.getBody();
    assertResponseCode(stockResponse, HttpStatus.OK);
    assertThat(stocks).hasSameSizeAs(expectedStocks);
    assertThat(stocks).hasSameElementsAs(expectedStocks);
  }
}