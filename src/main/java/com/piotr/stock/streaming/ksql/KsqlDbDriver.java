package com.piotr.stock.streaming.ksql;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ExecuteStatementResult;
import io.confluent.ksql.api.client.Row;
import io.confluent.ksql.api.client.StreamedQueryResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KsqlDbDriver {

  private static final Map<String, Object> PROPERTIES = Collections.singletonMap("auto.offset.reset", "earliest");

  private final Client ksqlClient;

  public CompletableFuture<ExecuteStatementResult> executeSql(String sql) {
    return ksqlClient.executeStatement(sql, PROPERTIES);
  }

  @SneakyThrows
  public List<Row> findByQuery(String sql) {
    StreamedQueryResult streamedQueryResult = ksqlClient
        .streamQuery(sql, PROPERTIES)
        .get();

    Row row;
    List<Row> results = new ArrayList<>();
    while ((row = streamedQueryResult.poll()) != null) {
      results.add(row);
    }
    return results;
  }
}
