package com.piotr.stock.streaming.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.rest.model.Stock;
import com.piotr.stock.streaming.util.StockBuilder;
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

    return new StockBuilder()
        .withTicker("BTC/USD")
        .withQuoteType("Digital Currency")
        .withStockMarket("Binance")
        .withPrice(BigDecimal.valueOf(43993.0))
        .withCurrency("US Dollar")
        .withVolume(72110L)
        .withStockTimestamp(stockTimestamp)
        .build();
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