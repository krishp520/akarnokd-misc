package hu.akarnokd.rxreactor;

import java.lang.reflect.*;
import java.util.*;

import reactor.core.publisher.Flux;
import rx.Observable;

public final class RxReactorDiff {

    private RxReactorDiff() { }

    static void dumpClass(Class<?> cl) {
        Method[] methods = cl.getMethods();
        Arrays.sort(methods, createMethodComparator());

        int count = 0;
        for (Method method : methods) {
            if (method.getDeclaringClass() == cl) {
                printMethodInfo(method);
                count++;
            }
        }

        System.out.println("---");
        System.out.println(count);
    }

    private static Comparator<Method> createMethodComparator() {
        return (a, b) -> {
            int d = Modifier.isStatic(a.getModifiers()) ? 0 : 1;
            int e = Modifier.isStatic(b.getModifiers()) ? 0 : 1;
            int f = Integer.compare(d, e);
            if (f == 0) {
                f = a.getName().compareTo(b.getName());
                if (f == 0) {
                    f = a.toString().compareTo(b.toString());
                }
            }
            return f;
        };
    }

    private static void printMethodInfo(Method method) {
        if (Modifier.isStatic(method.getModifiers())) {
            System.out.print("static ");
        }
        String methodString = processMethodString(method.toString());
        System.out.println(methodString);
    }

    private static String processMethodString(String methodString) {
        String s = methodString;
        String str = "rx.Observable.";
        int i = s.indexOf(str);
        if (i >= 0) {
            s = s.substring(i + str.length());
        }
        str = "reactor.core.publisher.Flux.";
        i = s.indexOf(str);
        if (i >= 0) {
            s = s.substring(i + str.length());
        }

        s = s.replaceAll("java\\.util\\.concurrent\\.", "");
        s = s.replaceAll("java\\.util\\.concurrent\\.", "");
        return s;
    }

    public static void main(String[] args) {
        dumpClass(Observable.class);
        System.out.println("--");
        dumpClass(Flux.class);
    }
}
