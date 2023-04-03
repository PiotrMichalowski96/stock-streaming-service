package com.piotr.stock.streaming.rest.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.piotr.stock.streaming.it.util.JsonUtil;
import com.piotr.stock.streaming.model.ApiRequestParams;
import com.piotr.stock.streaming.repository.StockQueryInfo;
import com.piotr.stock.streaming.rest.model.Stock;
import com.piotr.stock.streaming.route.caller.RetrieveStockRouteCaller;
import com.piotr.stock.streaming.route.caller.SaveStockRouteCaller;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class StockApiDelegateImplTest {

  @Mock
  private RetrieveStockRouteCaller retrieveStockRouteCaller;

  @Mock
  private SaveStockRouteCaller saveStockRouteCaller;

  private StockApiDelegateImpl stockApiDelegate;

  @BeforeEach
  void init() {
    Mockito.reset(retrieveStockRouteCaller);
    Mockito.reset(saveStockRouteCaller);
    stockApiDelegate = new StockApiDelegateImpl(retrieveStockRouteCaller, saveStockRouteCaller);
  }

  @Test
  void shouldCallSaveStockRoute() throws IOException {
    //given
    Stock stock = JsonUtil.readJsonFile("samples/other/stock.json", Stock.class);
    mockSaveStockRoute(stock);

    //when
    ResponseEntity<Stock> response = stockApiDelegate.addStock(stock);

    //then
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(stock);
  }

  @ParameterizedTest
  @MethodSource("provideStocksAndResponse")
  void shouldCallFindStockRoute(List<Stock> stocks, ResponseEntity<List<Stock>> exchangeResponse) {
    //given
    var ticker = "BTC/EUR";
    var stockType = "Digital Currency";
    var exchange = "NASDAQ";
    var apiRequestParams = new ApiRequestParams(ticker, stockType, exchange);
    var stockQueryInfo = new StockQueryInfo(apiRequestParams);

    mockFindStockRoute(stockQueryInfo, stocks);

    //when
    ResponseEntity<List<Stock>> actualResponse = stockApiDelegate.stock(ticker, stockType, exchange);

    //then
    assertThat(actualResponse).usingRecursiveComparison().isEqualTo(exchangeResponse);
  }

  private static Stream<Arguments> provideStocksAndResponse() throws IOException {
    List<Stock> stocks = JsonUtil.readJsonArrayFile("samples/other/stocks.json", Stock.class);

    return Stream.of(
        Arguments.of(stocks, ResponseEntity.ok(stocks)),
        Arguments.of(Collections.emptyList(), new ResponseEntity<>(HttpStatus.NO_CONTENT))
    );
  }

  private void mockSaveStockRoute(Stock inOut) {
    when(saveStockRouteCaller.callRoute(inOut)).thenReturn(inOut);
  }

  private void mockFindStockRoute(StockQueryInfo input, List<Stock> output) {
    when(retrieveStockRouteCaller.callRoute(input)).thenReturn(output);
  }
}