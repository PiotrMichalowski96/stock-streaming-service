package com.piotr.stock.streaming.it.util;

import static org.awaitility.Awaitility.given;

import java.time.Duration;
import lombok.experimental.UtilityClass;
import org.awaitility.core.ThrowingRunnable;

@UtilityClass
public class AwaitilityUtil {

  private static final Duration TEST_TIMEOUT = Duration.ofSeconds(30);

  public static void testAsynchronous(final ThrowingRunnable assertion) {
    given().ignoreExceptions()
        .await()
        .atMost(TEST_TIMEOUT)
        .untilAsserted(assertion);
  }
}
