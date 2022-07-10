package com.piotr.stock.streaming.mapper;


import static org.assertj.core.api.Assertions.assertThat;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.rest.model.Stock;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class StockMapperTest {

  private final StockMapper stockMapper = new StockMapperImpl();

  @Test
  void shouldMapEntityToDto() {
    //given
    StockEntity stockEntity = createStockEntity();
    Stock expectedStockDto = createStockDto();

    //when
    Stock actualStockDto = stockMapper.toStockDto(stockEntity);

    //then
    assertThat(actualStockDto).usingRecursiveComparison().isEqualTo(expectedStockDto);
  }

  private Stock createStockDto() {
    ZoneId zone = ZoneId.of("Europe/Warsaw");
    OffsetDateTime stockTimestamp = LocalDateTime.of(2022, 2, 11, 20, 52, 48)
        .atZone(zone).toOffsetDateTime();

    Stock stock = new Stock();
    stock.setTicker("BTC/USD");
    stock.setQuoteType("Digital Currency");
    stock.setStockMarket("Binance");
    stock.setPrice(BigDecimal.valueOf(43993.0));
    stock.setCurrency("US Dollar");
    stock.setVolume(72110L);
    stock.setStockTimestamp(stockTimestamp);
    return stock;
  }

  private StockEntity createStockEntity() {
    LocalDateTime stockTimestamp = LocalDateTime.of(2022, 2, 11, 20, 52, 48);
    return StockEntity.builder()
        .id(1L)
        .ticker("BTC/USD")
        .stockType("Digital Currency")
        .exchange("Binance")
        .price(BigDecimal.valueOf(43993.0))
        .currency("US Dollar")
        .volume(BigInteger.valueOf(72110L))
        .stockTimestamp(stockTimestamp)
        .build();
  }
}