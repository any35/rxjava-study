package com.github.zouzhberk.study.rxjava2;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Created by clouder on 12/9/16.
 */
public class ConnectableFlowableDemo
{
    @Test
    public void testConnectableFlowable() throws InterruptedException
    {
        ConnectableFlowable<String> f1 = Flowable.generate(() -> new BufferedReader(new
                        InputStreamReader(System.in))
                , (reader, e) -> {
                    while (true) {
                        String line = reader.readLine();
                        if (line == null || line.equalsIgnoreCase("exit")) {
                            break;
                        }
                        e.onNext(line);
                    }
                    e.onComplete();
                }).ofType(String.class).subscribeOn(Schedulers.io()).publish();

        TimeUnit.SECONDS.sleep(5);
        f1.connect(System.out::println);


        //f1.replay()

        TimeUnit.SECONDS.sleep(5);
        f1.observeOn(Schedulers.newThread()).map(x -> "s0- " + x).subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(5);
        f1.map(x -> "s1- " + x).subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(50);


    }

    public static void main(String[] args) throws InterruptedException
    {
        Consumer<Object> consumer = x -> System.out
                .println("Thread[" + Thread.currentThread().getName() + " ," + Thread
                        .currentThread().getId() + "] :" + x);

        ConnectableFlowable<String> f1 = from(System.in);
        TimeUnit.SECONDS.sleep(10);


        f1.connect(System.out::println);

        TimeUnit.SECONDS.sleep(10);
        f1.observeOn(Schedulers.newThread()).map(x -> "connenect- " + x).subscribe(consumer);

        TimeUnit.SECONDS.sleep(5);

        f1.map(x -> "connenect1- " + x).subscribe(consumer);
        TimeUnit.SECONDS.sleep(50);
    }

    public static ConnectableFlowable<String> from(InputStream inputStream)
    {
        return Flowable.generate(() -> {
            return new BufferedReader(new InputStreamReader(inputStream));
        }, (reader, e) -> {
            while (true) {
                String line = reader.readLine();

                if (line == null || line.equalsIgnoreCase("exit")) {
                    break;
                }
                System.out.println("reader: " + line);
                e.onNext(line);
            }
            e.onComplete();
        }).ofType(String.class).subscribeOn(Schedulers.io()).doOnNext(System.out::println)
                .publish();
    }

}
