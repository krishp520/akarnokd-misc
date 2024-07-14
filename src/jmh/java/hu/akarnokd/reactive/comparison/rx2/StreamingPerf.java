package hu.akarnokd.reactive.comparison.rx2;

import java.util.*;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import hu.akarnokd.reactive.comparison.consumers.PerfConsumer;
import io.reactivex.Flowable;
import io.reactivex.Observable;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(value = 1)
@State(Scope.Thread)
public class StreamingPerf {

    @Param({"1", "10", "100", "1000", "10000", "100000", "1000000"})
    public int count;

    // Abstract class for common setup and benchmarking logic
    abstract class BaseBenchmark<T> {
        Flowable<T> flowable;
        Observable<T> observable;

        public void setupFlowable(Flowable<Integer> flowable) {
            this.flowable = (Flowable<T>) flowable;
        }

        public void setupObservable(Observable<T> observable) {
            this.observable = observable;
        }

        public void benchmarkFlowable(Blackhole bh) {
            flowable.subscribe(new PerfConsumer(bh));
        }

        public void benchmarkObservable(Blackhole bh) {
            observable.subscribe(new PerfConsumer(bh));
        }
    }

    class FlowableBenchmark extends BaseBenchmark<Integer> {
        @Setup
        public void setup() {
            Integer[] array = new Integer[count];
            Arrays.fill(array, 777);
            setupFlowable(Flowable.fromArray(array));
            setupFlowable(Flowable.range(1, count));
            setupFlowable(Flowable.just(1, 2, 3, 4, 5));
        }

        @Benchmark
        public void benchmark(Blackhole bh) {
            benchmarkFlowable(bh);
        }
    }

    class ObservableBenchmark extends BaseBenchmark<Integer> {
        @Setup
        public void setup() {
            Integer[] array = new Integer[count];
            Arrays.fill(array, 777);
            setupObservable(Observable.fromArray(array));
            setupObservable(Observable.range(1, count));
            setupObservable(Observable.just(1, 2, 3, 4, 5));
        }

        @Benchmark
        public void benchmark(Blackhole bh) {
            benchmarkObservable(bh);
        }
    }
}
