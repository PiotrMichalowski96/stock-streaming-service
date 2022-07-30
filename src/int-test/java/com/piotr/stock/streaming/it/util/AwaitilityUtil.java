package com.piotr.stock.streaming.it.util;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.given;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.awaitility.core.ThrowingRunnable;

@UtilityClass
public class AwaitilityUtil {

  private static final Duration TEST_TIMEOUT = Duration.ofSeconds(30);
  private static final Duration CALL_TIMEOUT = Duration.ofMinutes(1);
  private static final Duration POLL_INTERVAL = Duration.ofSeconds(10);

  public static void testAsynchronous(final ThrowingRunnable assertion) {
    given().ignoreExceptions()
        .await()
        .atMost(TEST_TIMEOUT)
        .untilAsserted(assertion);
  }

  public static <T> T callUntil(Callable<T> supplier, Predicate<T> predicate) {
    return await()
        .timeout(CALL_TIMEOUT)
        .pollInterval(POLL_INTERVAL)
        .until(supplier, predicate);
  }
}
