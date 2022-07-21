package com.piotr.stock.streaming.mapper;

import static com.piotr.stock.streaming.util.TestDataFactoryUtil.createStockDto;
import static com.piotr.stock.streaming.util.TestDataFactoryUtil.createStockEntity;
import static org.assertj.core.api.Assertions.assertThat;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.rest.model.Stock;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StockMapperTest {

  private final StockMapper stockMapper = new StockMapperImpl();

  @ParameterizedTest
  @MethodSource("provideDtoAndEntity")
  void shouldMapEntityToDto(Stock expectedStockDto, StockEntity stockEntity) {
    //given input
    //when
    Stock actualStockDto = stockMapper.toStockDto(stockEntity);

    //then
    assertThat(actualStockDto).usingRecursiveComparison().isEqualTo(expectedStockDto);
  }

  @ParameterizedTest
  @MethodSource("provideDtoAndEntity")
  void shouldMapDtoToEntity(Stock stockDto, StockEntity expectedStockEntity) {
    //given input
    //when
    StockEntity actualStockEntity = stockMapper.toStockEntity(stockDto);

    //then
    assertThat(actualStockEntity).usingRecursiveComparison().isEqualTo(expectedStockEntity);
  }

  private static Stream<Arguments> provideDtoAndEntity() {
    LocalDateTime stockLocalDateTime = LocalDateTime.of(2022, 2, 11, 20, 52, 48);
    ZoneId zone = ZoneId.of("Europe/Warsaw");
    OffsetDateTime stockOffsetDateTime = LocalDateTime.of(2022, 2, 11, 20, 52, 48)
        .atZone(zone).toOffsetDateTime();

    return Stream.of(
        Arguments.of(createStockDto(stockOffsetDateTime), createStockEntity(123753267L, stockLocalDateTime)),
        Arguments.of(createStockDto(null), createStockEntity(-1651435411L, null))
    );
  }
}