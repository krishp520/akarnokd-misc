package hu.akarnokd.reactive.observables;

import java.util.*;
import java.util.concurrent.Callable;

import io.reactivex.functions.*;
import io.reactivex.internal.util.ExceptionHelper;

/**
 * Synchronous-only, non-backpressured reactive type.
 * @param <T> the element type
 */
public interface IObservable<T> {

    void subscribe(IObserver<? super T> observer);

    static <T> IObservable<T> just(T element) {
        return o -> {
            BooleanDisposable d = new BooleanDisposable();
            o.onSubscribe(d);
            if (!d.isDisposed()) {
                o.onNext(element);
                if (!d.isDisposed()) {
                    o.onComplete();
                }
            }
        };
    }

    static IObservable<Integer> characters(CharSequence cs) {
        return o -> {
            BooleanDisposable d = new BooleanDisposable();
            o.onSubscribe(d);
            for (int i = 0; i < cs.length(); i++) {
                if (d.isDisposed()) {
                    return;
                }
                o.onNext((int)cs.charAt(i));
            }
            if (!d.isDisposed()) {
                o.onComplete();
            }
        };
    }

    @SafeVarargs
    static <T> IObservable<T> concatArray(IObservable<T>... sources) {
        return o -> {
            ConcatObserver<T> obs = new ConcatObserver<>(o, sources);
            o.onSubscribe(obs);
            obs.onComplete();
        };
    }

    static <T> IObservable<T> fromIterable(Iterable<? extends T> source) {
        return o -> {
            BooleanDisposable d = new BooleanDisposable();
            o.onSubscribe(d);
            for (T item : source) {
                if (d.isDisposed()) {
                    return;
                }
                o.onNext(item);
            }
            if (!d.isDisposed()) {
                o.onComplete();
            }
        };
    }

    default <C> IObservable<C> collect(Callable<C> collectionSupplier, BiConsumer<? super C, ? super T> collector) {
        return o -> {
            C collection;
            try {
                collection = collectionSupplier.call();
            } catch (Throwable ex) {
                BooleanDisposable d = new BooleanDisposable();
                o.onSubscribe(d);
                o.onError(ex);
                return;
            }
            Collector<T, C> collectorObserver = new Collector<>(o, collection, collector);
            subscribe(collectorObserver);
        };
    }

    default <R> IObservable<R> flatMapIterable(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return o -> {
            FlatMapper<T, R> flatMapper = new FlatMapper<>(o, mapper);
            subscribe(flatMapper);
        };
    }

    default <R> IObservable<R> map(Function<? super T, ? extends R> mapper) {
        return o -> {
            Mapper<T, R> mapperObserver = new Mapper<>(o, mapper);
            subscribe(mapperObserver);
        };
    }

    default IObservable<T> filter(Predicate<? super T> predicate) {
        return o -> {
            Filterer<T> filterer = new Filterer<>(o, predicate);
            subscribe(filterer);
        };
    }

    @SuppressWarnings("unchecked")
    default IObservable<Integer> sumInt() {
        return o -> {
            SumInt sumIntObserver = new SumInt(o);
            ((IObservable<Number>) this).subscribe(sumIntObserver);
        };
    }

    @SuppressWarnings("unchecked")
    default IObservable<Long> sumLong() {
        return o -> {
            SumLong sumLongObserver = new SumLong(o);
            ((IObservable<Number>) this).subscribe(sumLongObserver);
        };
    }

    @SuppressWarnings("unchecked")
    default IObservable<Integer> maxInt() {
        return o -> {
            MaxInt maxIntObserver = new MaxInt(o);
            ((IObservable<Number>) this).subscribe(maxIntObserver);
        };
    }

    default IObservable<T> take(long n) {
        return o -> {
            Take<T> takeObserver = new Take<>(o, n);
            subscribe(takeObserver);
        };
    }

    default IObservable<T> skip(long n) {
        return o -> {
            Skip<T> skipObserver = new Skip<>(o, n);
            subscribe(skipObserver);
        };
    }

    default T first() {
        First<T> firstObserver = new First<>();
        subscribe(firstObserver);
        if (firstObserver.error != null) {
            throw ExceptionHelper.wrapOrThrow(firstObserver.error);
        }
        if (firstObserver.item == null) {
            throw new NoSuchElementException();
        }
        return firstObserver.item;
    }

    default T last() {
        Last<T> lastObserver = new Last<>();
        subscribe(lastObserver);
        if (lastObserver.error != null) {
            throw ExceptionHelper.wrapOrThrow(lastObserver.error);
        }
        if (lastObserver.item == null) {
            throw new NoSuchElementException();
        }
        return lastObserver.item;
    }
}

// Separate classes for observers

class ConcatObserver<T> implements IObserver<T>, IDisposable {
    final IObserver<? super T> o;
    final IObservable<T>[] sources;

    IDisposable d;
    boolean disposed;
    int wip;
    int index;

    ConcatObserver(IObserver<? super T> o, IObservable<T>[] sources) {
        this.o = o;
        this.sources = sources;
    }

    @Override
    public void dispose() {
        disposed = true;
        d.dispose();
    }

    @Override
    public void onSubscribe(IDisposable d) {
        if (disposed) {
            d.dispose();
        } else {
            this.d = d;
        }
    }

    @Override
    public void onNext(T element) {
        o.onNext(element);
    }

    @Override
    public void onError(Throwable cause) {
        o.onError(cause);
    }

    @Override
    public void onComplete() {
        if (wip++ == 0) {
            do {
                if (disposed) {
                    return;
                }
                if (index == sources.length) {
                    o.onComplete();
                    return;
                }
                sources[index++].subscribe(this);
            } while (--wip != 0);
        }
    }
}

class Collector<T, C> implements IObserver<T>, IDisposable {
    final IObserver<? super C> o;
    final C collection;
    final BiConsumer<? super C, ? super T> collector;
    IDisposable d;
    boolean disposed;

    Collector(IObserver<? super C> o, C collection, BiConsumer<? super C, ? super T> collector) {
        this.o = o;
        this.collection = collection;
        this.collector = collector;
    }

    @Override
    public void dispose() {
        disposed = true;
        d.dispose();
    }

    @Override
    public void onSubscribe(IDisposable d) {
        this.d = d;
        o.onSubscribe(this);
    }

    @Override
    public void onNext(T element) {
        try {
            collector.accept(collection, element);
        } catch (Throwable ex) {
            dispose();
            o.onError(ex);
        }
    }

    @Override
    public void onError(Throwable cause) {
        o.onError(cause);
    }

    @Override
    public void onComplete() {
        o.onNext(collection);
        if (!disposed) {
            o.onComplete();
        }
    }
}

class FlatMapper<T, R> implements IObserver<T>, IDisposable {
    final IObserver<? super R> o;
    final Function<? super T, ? extends Iterable<? extends R>> mapper;
    IDisposable d;
    boolean disposed;

    FlatMapper(IObserver<? super R> o, Function<? super T, ? extends Iterable<? extends R>> mapper) {
        this.o = o;
        this.mapper = mapper;
    }

    @Override
    public void dispose() {
        disposed = true;
        d.dispose();
    }

    @Override
    public void onSubscribe(IDisposable d) {
        this.d = d;
        o.onSubscribe(this);
    }

    @Override
    public void onNext(T element) {
        Iterator<? extends R> it;
        try {
            it = mapper.apply(element).iterator();
        } catch (Throwable ex) {
            dispose();
            o.onError(ex);
            return;
        }
        while (it.hasNext()) {
            if (disposed) {
                break;
            }
            o.onNext(it.next());
        }
    }

    @Override
    public void onError(Throwable cause) {
        o.onError(cause);
    }

    @Override
    public void onComplete() {
        o.onComplete();
    }
}

class Mapper<T, R> implements IObserver<T>, IDisposable {
    final IObserver<? super R> o;
    final Function<? super T, ? extends R> mapper;
    IDisposable d;
    boolean disposed;

    Mapper(IObserver<? super R> o, Function<? super T, ? extends R> mapper) {
        this.o = o;
        this.mapper = mapper;
    }

    @Override
    public void dispose() {
        disposed = true;
        d.dispose();
    }

    @Override
    public void onSubscribe(IDisposable d) {
        this.d = d;
        o.onSubscribe(this);
    }

    @Override
    public void onNext(T element) {
        R result;
        try {
            result = mapper.apply(element);
        } catch (Throwable ex) {
            dispose();
            o.onError(ex);
            return;
        }
        o.onNext(result);
    }

    @Override
    public void onError(Throwable cause) {
        o.onError(cause);
    }

    @Override
    public void onComplete() {
        o.onComplete();
    }
}

class Filterer<T> implements IObserver<T>, IDisposable {
    final IObserver<? super T> o;
    final Predicate<? super T> predicate;
    IDisposable d;
    boolean disposed;

    Filterer(IObserver<? super T> o, Predicate<? super T> predicate) {
        this.o = o;
        this.predicate = predicate;
    }

    @Override
    public void dispose() {
        disposed = true;
        d.dispose();
    }

    @Override
    public void onSubscribe(IDisposable d) {
        this.d = d;
        o.onSubscribe(this);
    }

    @Override
    public void onNext(T element) {
        boolean b;
        try {
            b = predicate.test(element);
        } catch (Throwable ex) {
            dispose();
            o.onError(ex);
            return;
        }
        if (b) {
            o.onNext(element);
        }
    }

    @Override
    public void onError(Throwable cause) {
        o.onError(cause);
    }

    @Override
    public void onComplete() {
        o.onComplete();
    }
}

class SumInt implements IObserver<Number>, IDisposable {
    final IObserver<? super Integer> o;
    IDisposable d;
    boolean disposed;
    int sum;

    SumInt(IObserver<? super Integer> o) {
        this.o = o;
    }

    @Override
    public void dispose() {
        disposed = true;
        d.dispose();
    }

    @Override
    public void onSubscribe(IDisposable d) {
        this.d = d;
        o.onSubscribe(this);
    }

    @Override
    public void onNext(Number element) {
        sum += element.intValue();
    }

    @Override
    public void onError(Throwable cause) {
        o.onError(cause);
    }

    @Override
    public void onComplete() {
        o.onNext(sum);
        o.onComplete();
    }
}

class SumLong implements IObserver<Number>, IDisposable {
    final IObserver<? super Long> o;
    IDisposable d;
    boolean disposed;
    long sum;

    SumLong(IObserver<? super Long> o) {
        this.o = o;
    }

    @Override
    public void dispose() {
        disposed = true;
        d.dispose();
    }

    @Override
    public void onSubscribe(IDisposable d) {
        this.d = d;
        o.onSubscribe(this);
    }

    @Override
    public void onNext(Number element) {
        sum += element.longValue();
    }

    @Override
    public void onError(Throwable cause) {
        o.onError(cause);
    }

    @Override
    public void onComplete() {
        o.onNext(sum);
        o.onComplete();
    }
}

class MaxInt implements IObserver<Number>, IDisposable {
    final IObserver<? super Integer> o;
    IDisposable d;
    boolean disposed;
    int max = Integer.MIN_VALUE;

    MaxInt(IObserver<? super Integer> o) {
        this.o = o;
    }

    @Override
    public void dispose() {
        disposed = true;
        d.dispose();
    }

    @Override
    public void onSubscribe(IDisposable d) {
        this.d = d;
        o.onSubscribe(this);
    }

    @Override
    public void onNext(Number element) {
        max = Math.max(max, element.intValue());
    }

    @Override
    public void onError(Throwable cause) {
        o.onError(cause);
    }

    @Override
    public void onComplete() {
        o.onNext(max);
        o.onComplete();
    }
}

class Take<T> implements IObserver<T>, IDisposable {
    final IObserver<? super T> o;
    final long n;
    IDisposable d;
    boolean disposed;
    long remaining;

    Take(IObserver<? super T> o, long n) {
        this.o = o;
        this.n = n;
        this.remaining = n;
    }

    @Override
    public void dispose() {
        disposed = true;
        d.dispose();
    }

    @Override
    public void onSubscribe(IDisposable d) {
        this.d = d;
        o.onSubscribe(this);
    }

    @Override
    public void onNext(T element) {
        if (remaining-- > 0) {
            o.onNext(element);
        }
        if (remaining == 0) {
            onComplete();
        }
    }

    @Override
    public void onError(Throwable cause) {
        o.onError(cause);
    }

    @Override
    public void onComplete() {
        o.onComplete();
        dispose();
    }
}

class Skip<T> implements IObserver<T>, IDisposable {
    final IObserver<? super T> o;
    final long n;
    IDisposable d;
    boolean disposed;
    long remaining;

    Skip(IObserver<? super T> o, long n) {
        this.o = o;
        this.n = n;
        this.remaining = n;
    }

    @Override
    public void dispose() {
        disposed = true;
        d.dispose();
    }

    @Override
    public void onSubscribe(IDisposable d) {
        this.d = d;
        o.onSubscribe(this);
    }

    @Override
    public void onNext(T element) {
        if (remaining-- <= 0) {
            o.onNext(element);
        }
    }

    @Override
    public void onError(Throwable cause) {
        o.onError(cause);
    }

    @Override
    public void onComplete() {
        o.onComplete();
    }
}

class First<T> implements IObserver<T> {
    T item;
    Throwable error;
    IDisposable d;
    boolean disposed;

    @Override
    public void dispose() {
        disposed = true;
        d.dispose();
    }

    @Override
    public void onSubscribe(IDisposable d) {
        this.d = d;
    }

    @Override
    public void onNext(T element) {
        item = element;
        onComplete();
    }

    @Override
    public void onError(Throwable cause) {
        error = cause;
    }

    @Override
    public void onComplete() {
        if (d != null) {
            d.dispose();
        }
    }
}

class Last<T> implements IObserver<T> {
    T item;
    Throwable error;
    IDisposable d;
    boolean disposed;

    @Override
    public void dispose() {
        disposed = true;
        d.dispose();
    }

    @Override
    public void onSubscribe(IDisposable d) {
        this.d = d;
    }

    @Override
    public void onNext(T element) {
        item = element;
    }

    @Override
    public void onError(Throwable cause) {
        error = cause;
    }

    @Override
    public void onComplete() {
        if (d != null) {
            d.dispose();
        }
    }
}
