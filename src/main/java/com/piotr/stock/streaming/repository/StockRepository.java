package com.piotr.stock.streaming.repository;

import com.piotr.stock.streaming.entity.StockEntity;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StockRepository {

  private static final Class<StockEntity> ENTITY_CLASS = StockEntity.class;
  private static final int LIMIT = 2;

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<StockEntity> findStockList(StockQueryInfo stockQueryInfo) {
    Map<String, String> params = stockQueryInfo.getQueryParams();
    String sqlQuery = new SqlQueryBuilder()
        .withParameters(params)
        .build();
    SqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
    BeanPropertyRowMapper<StockEntity> propertyRowMapper = new BeanPropertyRowMapper<>(ENTITY_CLASS);
    return namedParameterJdbcTemplate.query(sqlQuery, sqlParameterSource, propertyRowMapper);
  }

  public List<StockEntity> findStockLimited() {
    String sqlQuery = new SqlQueryBuilder()
        .withLimit(LIMIT)
        .build();
    BeanPropertyRowMapper<StockEntity> propertyRowMapper = new BeanPropertyRowMapper<>(ENTITY_CLASS);
    return namedParameterJdbcTemplate.query(sqlQuery, propertyRowMapper);
  }
}
