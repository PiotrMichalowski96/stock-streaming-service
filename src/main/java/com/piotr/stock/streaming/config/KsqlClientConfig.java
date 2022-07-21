package com.piotr.stock.streaming.config;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka.ksql")
@Getter
@Setter
public class KsqlClientConfig {

  private String host;
  private int port;

  @Bean
  Client ksqlClient() {
    ClientOptions options = ClientOptions.create()
        .setHost(host)
        .setPort(port);
    return Client.create(options);
  }
}
