package com.piotr.stock.streaming.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class SqlQueryBuilderTest {

  @Test
  void shouldCreateSqlQueryWithOneParam() {
    //given
    String ksqlTable = "stock";
    Map<String, String> params = Map.of("ticker", "USD/EUR");
    String expectedSql = "SELECT * FROM STOCK WHERE TICKER = 'USD/EUR';";

    //when
    String actualSql = new SqlQueryBuilder(ksqlTable)
        .withParameters(params)
        .build();

    //then
    assertThat(actualSql).isEqualTo(expectedSql);
  }

  @Test
  void shouldCreateSqlQueryWithTwoParams() {
    //given
    String ksqlTable = "stock";
    Map<String, String> params = Map.of("ticker", "USD/EUR",
        "STOCK_TYPE", "Physical currency");

    //two expected SQL queries, because order of filter params does not matter
    String expectedSql1 = "SELECT * FROM STOCK WHERE TICKER = 'USD/EUR' AND STOCK_TYPE = 'Physical currency';";
    String expectedSql2 = "SELECT * FROM STOCK WHERE STOCK_TYPE = 'Physical currency' AND TICKER = 'USD/EUR';";

    //when
    String actualSql = new SqlQueryBuilder(ksqlTable)
        .withParameters(params)
        .build();

    //then
    assertThat(actualSql).satisfiesAnyOf(
        sql -> assertThat(sql).isEqualTo(expectedSql1),
        sql -> assertThat(sql).isEqualTo(expectedSql2)
    );
  }

  @Test
  void shouldCreateSqlQueryWithLimit() {
    //given
    String ksqlTable = "stock";
    String expectedSql = "SELECT * FROM STOCK LIMIT 10;";

    //when
    String actualSql = new SqlQueryBuilder(ksqlTable)
        .withLimit(10)
        .build();

    //then
    assertThat(actualSql).isEqualTo(expectedSql);
  }
}