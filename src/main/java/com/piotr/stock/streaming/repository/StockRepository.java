package com.piotr.stock.streaming.repository;

import com.piotr.stock.streaming.ksql.KsqlDbDriver;
import io.confluent.ksql.api.client.Row;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StockRepository {

  private static final int LIMIT = 10;

  @Value("${kafka.ksql.table}")
  private final String ksqlTable;
  private final KsqlDbDriver ksqlDbDriver;

  public List<Row> findStockRows(StockQueryInfo stockQueryInfo) {
    Map<String, String> params = stockQueryInfo.getQueryParams();
    String sqlQuery = new SqlQueryBuilder(ksqlTable)
        .withParameters(params)
        .build();
    return ksqlDbDriver.findByQuery(sqlQuery);
  }

  public List<Row> findStockRowsLimited() {
    String sqlQuery = new SqlQueryBuilder(ksqlTable)
        .withLimit(LIMIT)
        .build();
    return ksqlDbDriver.findByQuery(sqlQuery);
  }
}
