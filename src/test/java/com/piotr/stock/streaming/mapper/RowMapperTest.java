package com.piotr.stock.streaming.mapper;

import static com.piotr.stock.streaming.util.TestDataFactoryUtil.createKsqlDbRow;
import static com.piotr.stock.streaming.util.TestDataFactoryUtil.createStockEntity;
import static org.assertj.core.api.Assertions.assertThat;

import com.piotr.stock.streaming.entity.StockEntity;
import io.confluent.ksql.api.client.Row;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class RowMapperTest {

  private final RowMapper rowMapper = new RowMapper();

  @Test
  void shouldMapRowToStock() {
    //given
    Long id = 1L;
    LocalDateTime stockLocalDateTime = LocalDateTime.of(2022, 2, 11, 20, 52, 48);
    String stockTimestampText = "2022-02-11 20:52:48";

    Row ksqlDbRow = createKsqlDbRow(id, stockTimestampText);
    StockEntity expectedStock = createStockEntity(id, stockLocalDateTime);

    //when
    StockEntity actualStock = rowMapper.mapRowToStockEntity(ksqlDbRow);

    //then
    assertThat(actualStock).usingRecursiveComparison().isEqualTo(expectedStock);
  }
}