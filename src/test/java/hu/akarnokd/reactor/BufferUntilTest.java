package hu.akarnokd.reactor;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

public class BufferUntilTest {

    @Test
    public void test() {
        Hooks.onOperatorDebug();

        Flux.range(0, 1000)
        .bufferUntil(v -> true)
        .flatMap(v -> Mono.delay(Duration.ofMillis(10)))
        .blockLast();
    }
}
