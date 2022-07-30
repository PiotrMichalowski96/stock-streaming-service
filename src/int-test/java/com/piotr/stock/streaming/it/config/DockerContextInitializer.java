package com.piotr.stock.streaming.it.config;

import static com.piotr.stock.streaming.it.common.KsqlDbIntegrationTest.dockerComposeContainer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class DockerContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    dockerComposeContainer.start();
  }
}
