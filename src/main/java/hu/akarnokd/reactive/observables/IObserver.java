package hu.akarnokd.reactive.observables;

public interface IObserver<T> {

    void dispose();

    void onSubscribe(IDisposable d);

    void onNext(T element);

    void onError(Throwable cause);

    void onComplete();
}
