package hu.akarnokd.rxjava3;

import org.reactivestreams.Publisher;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.functions.Function;


public class UsingWhen {

    static class UsingWhenParams<D> {
        final Publisher<? extends D> resource;
        final Function<? super D, ? extends Publisher<?>> onComplete;
        final Function<? super D, ? extends Publisher<?>> onError;
        final Function<? super D, ? extends Publisher<?>> onCancel;

        UsingWhenParams(Publisher<? extends D> resource,
                        Function<? super D, ? extends Publisher<?>> onComplete,
                        Function<? super D, ? extends Publisher<?>> onError,
                        Function<? super D, ? extends Publisher<?>> onCancel) {
            this.resource = resource;
            this.onComplete = onComplete;
            this.onError = onError;
            this.onCancel = onCancel;
        }
    }

    static <T, D> Flowable<T> usingWhen(
            Publisher<? extends D> resource,
            Function<? super D, ? extends Publisher<? extends T>> use,
            Function<? super D, ? extends Publisher<?>> cleanup) {
        return
                Maybe.fromPublisher(resource)
                .flatMapPublisher(res ->
                    Flowable.using(
                            () -> res,
                            use,
                            resc -> Flowable.fromPublisher(cleanup.apply(resc)).subscribe(),
                            false
                    )
                );
    }



    static <T, D> Flowable<T> usingWhen(
            Function<? super D, ? extends Publisher<? extends T>> use,
            UsingWhenParams<D> params) {
        return
                Maybe.fromPublisher(params.resource)
                        .flatMapPublisher(res ->
                                Flowable.fromPublisher(use.apply(res))
                                        .flatMap(
                                                v -> Flowable.just(v),
                                                e -> Flowable.fromPublisher(params.onError.apply(res)).ignoreElements().toFlowable(),
                                                () -> Flowable.fromPublisher(params.onComplete.apply(res)).ignoreElements().toFlowable()
                                        )
                                        .doOnCancel(() -> Flowable.fromPublisher(params.onCancel.apply(res)).subscribe())
                        );
    }
}
