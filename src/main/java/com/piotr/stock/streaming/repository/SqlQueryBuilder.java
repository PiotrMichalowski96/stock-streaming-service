package com.piotr.stock.streaming.repository;

import static org.apache.commons.lang3.StringUtils.SPACE;

import com.piotr.stock.streaming.entity.StockEntity;
import java.util.Map;

public class SqlQueryBuilder {

  private final StringBuilder queryBuilder;

  public SqlQueryBuilder() {
    queryBuilder = new StringBuilder()
        .append("SELECT * FROM")
        .append(SPACE)
        .append(StockEntity.TABLE_NAME);
  }

  public SqlQueryBuilder withParameters(Map<String, String> filterParams) {
    if(filterParams.isEmpty()) {
      return this;
    }

    int paramIndex = 0;
    for(String param: filterParams.keySet()) {
      String clause = paramIndex == 0 ? "WHERE" : "AND";
      queryBuilder
          .append(SPACE)
          .append(clause)
          .append(SPACE)
          .append(param.toUpperCase())
          .append(SPACE)
          .append("=")
          .append(SPACE)
          .append(":")
          .append(param);
      paramIndex++;
    }
    return this;
  }

  public SqlQueryBuilder withLimit(int limit) {
    queryBuilder
        .append(SPACE)
        .append("LIMIT")
        .append(SPACE)
        .append(limit);
    return this;
  }

  public String build() {
    return queryBuilder.toString();
  }
}
