package hu.akarnokd.rxjava.javadoc;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Add "link rel='canonical' to the javadoc htmls of RxJava 1"
 */
public final class AddCanonical {

    private AddCanonical() { }

    static final Map<String, String> canonicals = new HashMap<>();


    public static void init() {
        addObservableMappings();
        addObserverMappings();
        addSchedulerMappings();
        addPluginMappings();
        addBackpressureMappings();
        addAnnotationMappings();
        addExceptionMappings();
        addFunctionMappings();
        addObservableTypeMappings();
        addSubscriptionMappings();
        addSingleMappings();
        addCompletableMappings();

        checkUrls();
    }

    private static void addObservableMappings() {
        canonicals.put("/rx/Observable.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/Flowable.html");
        canonicals.put("/rx/Observable.OnSubscribe.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/FlowableOnSubscribe.html");
        canonicals.put("/rx/Observable.Operator.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/FlowableOperator.html");
        canonicals.put("/rx/Observable.Transformer.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/FlowableTransformer.html");
        canonicals.put("/rx/Subscriber.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/FlowableSubscriber.html");
        canonicals.put("/rx/Emitter.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/FlowableEmitter.html");
    }

    private static void addObserverMappings() {
        canonicals.put("/rx/Observer.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/Observer.html");
        canonicals.put("/rx/Subscription.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/disposables/Disposable.html");
        canonicals.put("/rx/Notification.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/Notification.html");
    }

    private static void addSchedulerMappings() {
        canonicals.put("/rx/Scheduler.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/Scheduler.html");
        canonicals.put("/rx/Scheduler.Worker.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/Scheduler.Worker.html");
        canonicals.put("/rx/scheduler/Schedulers.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/schedulers/Schedulers.html");
        canonicals.put("/rx/scheduler/TestScheduler.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/schedulers/TestScheduler.html");
        canonicals.put("/rx/scheduler/TimeInterval.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/schedulers/Timed.html");
        canonicals.put("/rx/scheduler/Timestamped.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/schedulers/Timed.html");
        canonicals.put("/rx/scheduler/TrampolineScheduler.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/schedulers/Schedulers.html#trampoline%28%29");
    }

    private static void addPluginMappings() {
        canonicals.put("/rx/plugins/RxJavaPlugins.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/plugins/RxJavaPlugins.html");
        canonicals.put("/rx/plugins/RxJavaHooks.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/plugins/RxJavaPlugins.html");
        canonicals.put("/rx/plugins/RxJavaCompletableExecutionHook.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/plugins/RxJavaPlugins.html");
        canonicals.put("/rx/plugins/RxJavaSingleExecutionHook.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/plugins/RxJavaPlugins.html");
        canonicals.put("/rx/plugins/RxJavaObservableExecutionHook.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/plugins/RxJavaPlugins.html");
        canonicals.put("/rx/plugins/RxJavaSchedulersHook.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/plugins/RxJavaPlugins.html");
    }

    private static void addBackpressureMappings() {
        canonicals.put("/rx/BackpressureOverflow.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/BackpressureOverflowStrategy.html");
        canonicals.put("/rx/BackpressureOverflow.Strategy.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/BackpressureOverflowStrategy.html");
        canonicals.put("/rx/Emitter.BackpressureMode.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/BackpressureStrategy.html");
        canonicals.put("/rx/Producer.html",
                "http://www.reactive-streams.org/reactive-streams-1.0.0-javadoc/org/reactivestreams/Subscription.html");
    }

    private static void addAnnotationMappings() {
        canonicals.put("/rx/annotations/Beta.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/annotations/Beta.html");
        canonicals.put("/rx/annotations/Experimental.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/annotations/Experimental.html");
    }

    private static void addExceptionMappings() {
        canonicals.put("/rx/exceptions/Exceptions.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/exceptions/Exceptions.html");
        canonicals.put("/rx/exceptions/CompositeException.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/exceptions/CompositeException.html");
        canonicals.put("/rx/exceptions/MissingBackpressureException.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/exceptions/MissingBackpressureException.html");
        canonicals.put("/rx/exceptions/OnErrorNotImplementedException.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/exceptions/OnErrorNotImplementedException.html");
    }

    private static void addFunctionMappings() {
        canonicals.put("/rx/functions/Action0.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Action.html");
        canonicals.put("/rx/functions/Action1.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Consumer.html");
        canonicals.put("/rx/functions/Action2.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/BiConsumer.html");
        canonicals.put("/rx/functions/ActionN.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Consumer.html");
        canonicals.put("/rx/functions/Cancellable.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Cancellable.html");

        canonicals.put("/rx/functions/Func0.html",
                "https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Callable.html");
        canonicals.put("/rx/functions/Func1.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function.html");
        canonicals.put("/rx/functions/Func2.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/BiFunction.html");
        canonicals.put("/rx/functions/Func3.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function3.html");
        canonicals.put("/rx/functions/Func4.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function4.html");
        canonicals.put("/rx/functions/Func5.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function5.html");
        canonicals.put("/rx/functions/Func6.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function6.html");
        canonicals.put("/rx/functions/Func7.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function7.html");
        canonicals.put("/rx/functions/Func8.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function8.html");
        canonicals.put("/rx/functions/Func9.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function9.html");
        canonicals.put("/rx/functions/FuncN.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function.html");

        canonicals.put("/rx/functions/Action.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Action.html");
        canonicals.put("/rx/functions/BiConsumer.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/BiConsumer.html");
        canonicals.put("/rx/functions/BiFunction.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/BiFunction.html");
        canonicals.put("/rx/functions/BooleanSupplier.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/BooleanSupplier.html");
        canonicals.put("/rx/functions/Consumer.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Consumer.html");
        canonicals.put("/rx/functions/Function.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function.html");
        canonicals.put("/rx/functions/Function3.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function3.html");
        canonicals.put("/rx/functions/Function4.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function4.html");
        canonicals.put("/rx/functions/Function5.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function5.html");
        canonicals.put("/rx/functions/Function6.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function6.html");
        canonicals.put("/rx/functions/Function7.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function7.html");
        canonicals.put("/rx/functions/Function8.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function8.html");
        canonicals.put("/rx/functions/Function9.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/Function9.html");
        canonicals.put("/rx/functions/LongConsumer.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/functions/LongConsumer.html");
    }

    private static void addObservableTypeMappings() {
        canonicals.put("/rx/internal/operators/NotificationLite.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/internal/util/NotificationLite.html");
        canonicals.put("/rx/internal/operators/OperatorObserveOn.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/internal/operators/OperatorObserveOn.html");
        canonicals.put("/rx/internal/operators/BlockingOperatorToFuture.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/internal/operators/BlockingOperatorToFuture.html");
    }

    private static void addSubscriptionMappings() {
        canonicals.put("/rx/internal/subscriptions/Subscriptions.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/disposables/Disposables.html");
        canonicals.put("/rx/internal/subscriptions/MultipleAssignmentSubscription.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/disposables/SerialDisposable.html");
    }

    private static void addSingleMappings() {
        canonicals.put("/rx/internal/util/SubscriptionList.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/disposables/CompositeDisposable.html");
        canonicals.put("/rx/internal/util/OpenHashSet.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/internal/util/OpenHashSet.html");
    }

    private static void addCompletableMappings() {
        canonicals.put("/rx/singles/Single.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/Single.html");
        canonicals.put("/rx/singles/Single.OnSubscribe.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/SingleOnSubscribe.html");
        canonicals.put("/rx/singles/Single.Operator.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/SingleOperator.html");
        canonicals.put("/rx/singles/Single.Transformer.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/SingleTransformer.html");
        canonicals.put("/rx/completable/Completable.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/Completable.html");
        canonicals.put("/rx/completable/Completable.OnSubscribe.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/CompletableOnSubscribe.html");
        canonicals.put("/rx/completable/Completable.Operator.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/CompletableOperator.html");
        canonicals.put("/rx/completable/Completable.Transformer.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/CompletableTransformer.html");
    }

    private static void checkUrls() {
        for (Map.Entry<String, String> entry : canonicals.entrySet()) {
            try {
                URL url = new URL(entry.getValue());
                url.openStream().close();
                System.out.println(entry.getKey() + " -> " + entry.getValue() + " is valid.");
            } catch (IOException ex) {
                System.out.println("Invalid URL for " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    static void init2x() {
        Map<String, String> canonicals = new HashMap<>();
        // ----------------------------------------------------------------------------------

        canonicals.put("/io/reactivex/disposables/Disposables.html",
                "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/disposables/Disposable.html");

        // ----------------------------------------------------------------------------------

        int failed = 0;
        for (String urls : canonicals.values()) {
            System.out.print("Checking: " + urls);
            try {
                URL u = new URL(urls);
                u.openStream().close();
                System.out.println(" -> Success");
                Thread.sleep(100);
            } catch (IOException | InterruptedException ex) {
                System.err.println(ex);
                failed++;
            }
        }

        if (failed != 0) {
            throw new RuntimeException("Some url's don't connect!: " + failed);
        }
        AddCanonical.canonicals.putAll(canonicals);
    }

    static Set<String> reachable = new HashSet<>();
    static Set<String> unreachable = new HashSet<>();

    static boolean checkAndCacheURL(String url) {
        if (reachable.contains(url)) {
            return true;
        }
        if (unreachable.contains(url)) {
            return false;
        }
        try {
            URL u = new URL(url);
            u.openStream().close();
            System.out.println(" -> Success");
            Thread.sleep(100);
            reachable.add(url);
            return true;
        } catch (IOException | InterruptedException ex) {
            System.err.println(ex);
            unreachable.add(url);
            return false;
        }
    }

    static void process(File directory, String prefix) throws IOException {
        Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                String name = file.toString().replace('\\', '/');
                if (name.endsWith(".html")) {
                    int idx = name.indexOf(prefix);
                    String relevantPart = name.substring(idx);

                    String canonical = canonicals.get(relevantPart);

                    if (canonical != null) {
                        appendLink(file, canonical);
                    }
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    static byte[] alreadyThere = "<link rel=\"canonical\"".getBytes(StandardCharsets.ISO_8859_1);
    static byte[] linkEnd = "/>".getBytes(StandardCharsets.ISO_8859_1);
    static byte[] endHead = "</head>".getBytes(StandardCharsets.ISO_8859_1);

    static void appendLink(Path file, String url) throws IOException {
        byte[] data = Files.readAllBytes(file);

        int existing = arrayIndexOf(data, alreadyThere, 0);
        if (existing != -1) {
            int end = arrayIndexOf(data, linkEnd, existing);
            byte[] newData = new byte[data.length - (end + 2 - existing)];
            System.arraycopy(data, 0, newData, 0, existing);
            System.arraycopy(data, end + 2, newData, existing, data.length - end - 2);
            data = newData;
        }
        int endHeadIndex = arrayIndexOf(data, endHead, 0);
        if (endHeadIndex < 0) {
            System.out.printf("File %s has no </head>?!%n", file);
        } else {
            System.out.printf("Adding link %s to file %s.%n", url, file);
            byte[] toInsert = ("<link rel=\"canonical\" href=\"" + url + "\"/>").getBytes(StandardCharsets.ISO_8859_1);

            byte[] newData = new byte[data.length + toInsert.length];
            System.arraycopy(data, 0, newData, 0, endHeadIndex);
            System.arraycopy(toInsert, 0, newData, endHeadIndex, toInsert.length);
            System.arraycopy(data, endHeadIndex, newData, endHeadIndex + toInsert.length, data.length - endHeadIndex);
            Files.write(file, newData);
        }
    }

    static int arrayIndexOf(byte[] source, byte[] toFind, int start) {
        outer:
        for (int i = start; i < source.length - toFind.length; i++) {
            if (source[i] == toFind[0]) {
                for (int j = i + 1; j < i + toFind.length; j++) {
                    if (source[j] != toFind[j - i]) {
                        continue outer;
                    }
                }
                return i;
            }
        }
        return -1;
    }

    static void process2x(File directory) throws IOException {
        Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                String name = file.toString().replace('\\', '/');
                if (name.endsWith(".html")) {
                    int idx = name.indexOf("/io/reactivex/");
                    String relevantPart = name.substring(idx);

                    String canonical = canonicals.get(relevantPart);
                    if (canonical == null) {
                        relevantPart = name.substring(idx + 14);

                        if (relevantPart.contains("/")) {
                            canonical = "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/" + relevantPart;
                        } else {
                            canonical = "http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/" + relevantPart;
                        }

                        if (!checkAndCacheURL(canonical)) {
                            return FileVisitResult.CONTINUE;
                        }
                    }
                    appendLink(file, canonical);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void main(String[] args) throws IOException {
//        init();
        init2x();
//        process(new File("..\\RxJava\\javadoc\\rx"), "/rx/");
//        process(new File("..\\RxJava\\1.x\\javadoc\\rx"), "/rx/");
//        process2x(new File("..\\RxJava\\2.x\\javadoc\\io\\reactivex\\"));
        process2x(new File("..\\RxJava\\javadoc\\io\\reactivex\\"));
    }

}
