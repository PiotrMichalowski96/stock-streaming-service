package com.piotr.stock.streaming.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.piotr.stock.streaming.ksql.KsqlDbDriver;
import com.piotr.stock.streaming.mapper.RowMapper;
import com.piotr.stock.streaming.model.ApiRequestParams;
import com.piotr.stock.streaming.repository.StockQueryInfo;
import com.piotr.stock.streaming.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RetrieveStockProcessorTest {

  private static final String KSQL_TABLE = "STOCK";

  private RetrieveStockProcessor retrieveStockProcessor;

  @Mock
  private KsqlDbDriver ksqlDbDriver;

  @Captor
  private ArgumentCaptor<String> sqlQueryCaptor;

  @BeforeEach
  void setUp() {
    StockRepository stockRepository = new StockRepository(KSQL_TABLE, ksqlDbDriver);
    retrieveStockProcessor = new RetrieveStockProcessor(new RowMapper(), stockRepository);
  }

  @Test
  void shouldFindStocksByParameters() {
    //given
    ApiRequestParams apiRequestParams = new ApiRequestParams("BTC/USD", null, null);
    StockQueryInfo stockQueryInfo = new StockQueryInfo(apiRequestParams);
    String expectedSql = "SELECT * FROM STOCK WHERE TICKER = 'BTC/USD';";

    //when
    retrieveStockProcessor.process(stockQueryInfo);

    //then
    verify(ksqlDbDriver).findByQuery(sqlQueryCaptor.capture());

    String actualSql = sqlQueryCaptor.getValue();
    assertThat(actualSql).isEqualTo(expectedSql);
  }

  @Test
  void shouldFindStocksLimited() {
    //given
    ApiRequestParams apiRequestParams = new ApiRequestParams();
    StockQueryInfo stockQueryInfo = new StockQueryInfo(apiRequestParams);
    String expectedSql = "SELECT * FROM STOCK LIMIT 10;";

    //when
    retrieveStockProcessor.process(stockQueryInfo);

    //then
    verify(ksqlDbDriver).findByQuery(sqlQueryCaptor.capture());

    String actualSql = sqlQueryCaptor.getValue();
    assertThat(actualSql).isEqualTo(expectedSql);
  }
}