package com.piotr.stock.streaming.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.piotr.stock.streaming.ksql.KsqlDbDriver;
import io.confluent.ksql.api.client.ExecuteStatementResult;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KsqlTableInitializerTest {

  private static final String KSQL_TABLE = "stock";
  private static final String TOPIC = "stockTopic";

  private KsqlTableInitializer ksqlTableInitializer;

  @Mock
  private KsqlDbDriver ksqlDbDriver;

  @Captor
  private ArgumentCaptor<String> sqlCaptor;

  @BeforeEach
  void setUp() {
    ksqlTableInitializer = new KsqlTableInitializer(KSQL_TABLE, TOPIC, ksqlDbDriver);
  }

  @Test
  void shouldInitializeTableInKsqlDb() throws ExecutionException, InterruptedException {
    //given
    String expectedSql = """
      CREATE SOURCE TABLE IF NOT EXISTS stock (
          id BIGINT PRIMARY KEY,
          ticker VARCHAR,
          stockType VARCHAR,
          exchange VARCHAR,
          price DECIMAL(14, 4),
          currency VARCHAR,
          volume BIGINT,
          stockTimestamp VARCHAR
        ) WITH (
          KAFKA_TOPIC = 'stockTopic',
          VALUE_FORMAT= 'JSON',
          TIMESTAMP = 'stockTimestamp',
          TIMESTAMP_FORMAT = 'yyyy-MM-dd HH:mm:ss'
          );
      """;

    CompletableFuture<ExecuteStatementResult> mockResponse = mock(CompletableFuture.class);
    when(ksqlDbDriver.executeSql(any())).thenReturn(mockResponse);

    ExecuteStatementResult mockResult = mock(ExecuteStatementResult.class);
    when(mockResponse.get()).thenReturn(mockResult);
    when(mockResult.queryId()).thenReturn(Optional.of("1"));

    //when
    ksqlTableInitializer.run();

    //then
    verify(ksqlDbDriver).executeSql(sqlCaptor.capture());

    String actualSql = sqlCaptor.getValue();
    assertThat(actualSql).isEqualTo(expectedSql);
  }
}