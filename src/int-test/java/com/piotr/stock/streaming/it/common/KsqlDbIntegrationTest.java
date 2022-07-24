package com.piotr.stock.streaming.it.common;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.it.common.kafka.MessageConsumer;
import com.piotr.stock.streaming.it.common.kafka.MessageProducer;
import com.piotr.stock.streaming.it.common.kafka.StockDeserializer;
import com.piotr.stock.streaming.json.StockSerializer;
import java.io.File;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("INT-TEST")
@Testcontainers
public abstract class KsqlDbIntegrationTest {

  private static final File KSQLDB_COMPOSE_FILE = new File("src/int-test/resources/docker/docker-compose.yaml");

  @Container
  public static DockerComposeContainer<?> dockerComposeContainer =
      new DockerComposeContainer<>(KSQLDB_COMPOSE_FILE)
          .withServices("zookeeper", "broker", "ksqldb-server", "ksqldb-cli")
          .withExposedService("broker", 29092, Wait.forListeningPort()
              .withStartupTimeout(Duration.ofMinutes(2)))
          .withExposedService("ksqldb-server", 8088, Wait.forHealthcheck()
              .withStartupTimeout(Duration.ofMinutes(2)))
          .withLocalCompose(true);

  private final String bootstrap;
  private final String topic;
  private final MessageProducer<StockEntity> producer;

  public KsqlDbIntegrationTest(String bootstrap, String topic) {
    this.bootstrap = bootstrap;
    this.topic = topic;
    producer = new MessageProducer<>(bootstrap, new StockSerializer());
  }

  protected void sendStock(StockEntity stock) {
    producer.send(topic, stock);
  }

  protected List<StockEntity> readStocks() {
    MessageConsumer<StockEntity> consumer = new MessageConsumer<>(bootstrap,
        new StockDeserializer<>(StockEntity.class),
        List.of(topic));
    List<StockEntity> stocks = consumer.readRecords();
    consumer.close();
    return stocks;
  }
}
