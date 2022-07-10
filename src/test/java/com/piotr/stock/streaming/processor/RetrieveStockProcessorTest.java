package com.piotr.stock.streaming.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.piotr.stock.streaming.model.ApiRequestParams;
import com.piotr.stock.streaming.repository.StockQueryInfo;
import com.piotr.stock.streaming.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@ExtendWith(MockitoExtension.class)
class RetrieveStockProcessorTest {

  private RetrieveStockProcessor retrieveStockProcessor;

  @InjectMocks
  private StockRepository stockRepository;

  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Captor
  private ArgumentCaptor<String> sqlQueryCaptor;

  @BeforeEach
  void setUp() {
    retrieveStockProcessor = new RetrieveStockProcessor(stockRepository);
  }

  @Test
  void shouldFindStocksByParameters() {
    //given
    ApiRequestParams apiRequestParams = new ApiRequestParams("BTC/USD", null, null);
    StockQueryInfo stockQueryInfo = new StockQueryInfo(apiRequestParams);
    String expectedSql = "SELECT * FROM STOCK WHERE TICKER = :TICKER";

    //when
    retrieveStockProcessor.process(stockQueryInfo);

    //then
    verify(namedParameterJdbcTemplate).query(sqlQueryCaptor.capture(), any(SqlParameterSource.class),
        any(BeanPropertyRowMapper.class));

    String actualSql = sqlQueryCaptor.getValue();
    assertThat(actualSql).isEqualTo(expectedSql);
  }

  @Test
  void shouldFindStocksLimited() {
    //given
    ApiRequestParams apiRequestParams = new ApiRequestParams();
    StockQueryInfo stockQueryInfo = new StockQueryInfo(apiRequestParams);
    String expectedSql = "SELECT * FROM STOCK LIMIT 2";

    //when
    retrieveStockProcessor.process(stockQueryInfo);

    //then
    verify(namedParameterJdbcTemplate).query(sqlQueryCaptor.capture(), any(BeanPropertyRowMapper.class));

    String actualSql = sqlQueryCaptor.getValue();
    assertThat(actualSql).isEqualTo(expectedSql);
  }

}