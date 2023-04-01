package com.piotr.stock.streaming.util;

import static com.piotr.stock.streaming.entity.StockEntity.CURRENCY_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.EXCHANGE_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.STOCK_ID;
import static com.piotr.stock.streaming.entity.StockEntity.PRICE_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.STOCK_TIMESTAMP_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.STOCK_TYPE_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.TICKER_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.VOLUME_COLUMN;
import static io.confluent.ksql.api.client.util.RowUtil.columnTypesFromStrings;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.rest.model.Stock;
import io.confluent.ksql.api.client.ColumnType;
import io.confluent.ksql.api.client.Row;
import io.confluent.ksql.api.client.impl.RowImpl;
import io.vertx.core.json.JsonArray;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestDataFactoryUtil {

  public static Stock createStockDto(OffsetDateTime stockTimestamp) {
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

  public static StockEntity createStockEntity(Long id, LocalDateTime stockTimestamp) {
    return StockEntity.builder()
        .id(id)
        .ticker("BTC/USD")
        .stockType("Digital Currency")
        .exchange("Binance")
        .price(BigDecimal.valueOf(43993.0))
        .currency("US Dollar")
        .volume(72110L)
        .stockTimestamp(stockTimestamp)
        .build();
  }

  public static Row createKsqlDbRow(Long id, String stockTimestamp) {

    List<String> columnNames = List.of(STOCK_ID,
        TICKER_COLUMN,
        STOCK_TYPE_COLUMN,
        EXCHANGE_COLUMN,
        PRICE_COLUMN,
        CURRENCY_COLUMN,
        VOLUME_COLUMN,
        STOCK_TIMESTAMP_COLUMN);

    List<ColumnType> columnTypes = columnTypesFromStrings(List.of("BIGINT", "DECIMAL", "STRING"));

    JsonArray jsonArrayValues = new JsonArray(List.of(id,
        "BTC/USD",
        "Digital Currency",
        "Binance",
        43993.0,
        "US Dollar",
        72110L,
        stockTimestamp));

    Map<String, Integer> columnNameToIndex = IntStream.range(0, columnNames.size())
        .boxed()
        .collect(Collectors.toMap(columnNames::get, index -> index + 1));

    return new RowImpl(columnNames, columnTypes, jsonArrayValues, columnNameToIndex);
  }
}
