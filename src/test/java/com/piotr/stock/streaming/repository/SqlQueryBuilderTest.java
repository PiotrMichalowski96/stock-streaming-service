package com.piotr.stock.streaming.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class SqlQueryBuilderTest {

  @Test
  void shouldCreateSqlQueryWithOneParam() {
    //given
    Map<String, String> params = Map.of("ticker", "USD/EUR");
    String expectedSql = "SELECT * FROM STOCK WHERE TICKER = :ticker";

    //when
    String actualSql = new SqlQueryBuilder()
        .withParameters(params)
        .build();

    //then
    assertThat(actualSql).isEqualTo(expectedSql);
  }

  @Test
  void shouldCreateSqlQueryWithTwoParams() {
    //given
    Map<String, String> params = Map.of("ticker", "USD/EUR",
        "STOCK_TYPE", "Physical currency");

    //two expected SQL queries, because order of filter params does not matter
    String expectedSql1 = "SELECT * FROM STOCK WHERE TICKER = :ticker AND STOCK_TYPE = :STOCK_TYPE";
    String expectedSql2 = "SELECT * FROM STOCK WHERE STOCK_TYPE = :STOCK_TYPE AND TICKER = :ticker";

    //when
    String actualSql = new SqlQueryBuilder()
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
    String expectedSql = "SELECT * FROM STOCK LIMIT 10";

    //when
    String actualSql = new SqlQueryBuilder()
        .withLimit(10)
        .build();

    //then
    assertThat(actualSql).isEqualTo(expectedSql);
  }
}