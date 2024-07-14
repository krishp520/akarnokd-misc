package hu.akarnokd.reactor;

import org.junit.*;

import reactor.core.publisher.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FlatMapMonoVoid {

    @Test
    public void test() {
        int c[] = { 0 };
        Flux.range(1, 1000)
        .flatMap(v -> Mono.fromRunnable(() -> { c[0]++; }))
        .ignoreElements()
        .block();

        Assert.assertEquals(1000, c[0]);
    }
}
