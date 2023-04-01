package com.piotr.stock.streaming.mapper;

import static com.piotr.stock.streaming.entity.StockEntity.CURRENCY_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.EXCHANGE_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.STOCK_ID;
import static com.piotr.stock.streaming.entity.StockEntity.PRICE_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.STOCK_TIMESTAMP_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.STOCK_TYPE_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.TICKER_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.VOLUME_COLUMN;

import com.piotr.stock.streaming.entity.StockEntity;
import io.confluent.ksql.api.client.Row;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class RowMapper {

  private static final DateTimeFormatter KSQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public StockEntity mapRowToStockEntity(Row row) {
    return StockEntity.builder()
        .id(row.getLong(STOCK_ID))
        .ticker(row.getString(TICKER_COLUMN))
        .stockType(row.getString(STOCK_TYPE_COLUMN))
        .exchange(row.getString(EXCHANGE_COLUMN))
        .price(row.getDecimal(PRICE_COLUMN))
        .currency(row.getString(CURRENCY_COLUMN))
        .volume(row.getLong(VOLUME_COLUMN))
        .volume(row.getLong(VOLUME_COLUMN))
        .stockTimestamp(convertTimestamp(row, STOCK_TIMESTAMP_COLUMN))
        .build();
  }

  private LocalDateTime convertTimestamp(Row row, String column) {
    String timestamp = row.getString(column);
    return LocalDateTime.parse(timestamp, KSQL_FORMATTER);
  }
}
