package com.piotr.stock.streaming.bootstrap;

import com.piotr.stock.streaming.ksql.KsqlDbDriver;
import io.confluent.ksql.api.client.ExecuteStatementResult;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KsqlTableInitializer implements CommandLineRunner {

  private static final String CREATE_TABLE_TEMPLATE = """
      CREATE SOURCE TABLE IF NOT EXISTS %s (
          id BIGINT PRIMARY KEY,
          ticker VARCHAR,
          stockType VARCHAR,
          exchange VARCHAR,
          price DECIMAL(14, 4),
          currency VARCHAR,
          volume BIGINT,
          stockTimestamp VARCHAR
        ) WITH (
          KAFKA_TOPIC = '%s',
          PARTITIONS = 1,
          VALUE_FORMAT= 'JSON',
          TIMESTAMP = 'stockTimestamp',
          TIMESTAMP_FORMAT = 'yyyy-MM-dd HH:mm:ss'
          );
      """;

  @Value("${kafka.ksql.table}")
  private final String ksqlTable;
  @Value("${kafka.topic}")
  private final String topic;
  private final KsqlDbDriver ksqlDbDriver;

  @Override
  public void run(String... args) {
    loadKsqlDbTable();
  }

  private synchronized void loadKsqlDbTable() {
    try {
      String createTableSql = String.format(CREATE_TABLE_TEMPLATE, ksqlTable, topic);
      ExecuteStatementResult result = ksqlDbDriver.executeSql(createTableSql).get();
      logger.info("Create KSQL DB table result: {}", result.queryId().orElse(null));
    } catch (ExecutionException | InterruptedException e) {
      logger.error("Error while creating KSQL DB table: ", e);
      Thread.currentThread().interrupt();
    }
  }
}
