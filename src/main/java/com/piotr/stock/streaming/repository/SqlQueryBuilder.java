package com.piotr.stock.streaming.repository;

import static org.apache.commons.lang3.StringUtils.SPACE;

import java.util.Map;

public class SqlQueryBuilder {

  private static final String SELECT = "SELECT * FROM";
  private static final String WHERE = "WHERE";
  private static final String AND = "AND";
  private static final String LIMIT = "LIMIT";

  private final StringBuilder queryBuilder;

  public SqlQueryBuilder(String ksqlTable) {
    queryBuilder = new StringBuilder()
        .append(SELECT)
        .append(SPACE)
        .append(ksqlTable.toUpperCase());
  }

  public SqlQueryBuilder withParameters(Map<String, String> filterParams) {
    if(filterParams.isEmpty()) {
      return this;
    }

    int paramIndex = 0;
    for(var entry: filterParams.entrySet()) {
      String clause = paramIndex == 0 ? WHERE : AND;
      queryBuilder
          .append(SPACE)
          .append(clause)
          .append(SPACE)
          .append(entry.getKey().toUpperCase())
          .append(SPACE)
          .append("=")
          .append(SPACE)
          .append("'")
          .append(entry.getValue())
          .append("'");
      paramIndex++;
    }
    return this;
  }

  public SqlQueryBuilder withLimit(int limit) {
    queryBuilder
        .append(SPACE)
        .append(LIMIT)
        .append(SPACE)
        .append(limit);
    return this;
  }

  public String build() {
    return queryBuilder
        .append(";")
        .toString();
  }
}
