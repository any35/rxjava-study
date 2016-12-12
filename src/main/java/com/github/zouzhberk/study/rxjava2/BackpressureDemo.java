package com.github.zouzhberk.study.rxjava2;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.operators.flowable.FlowableInternalHelper;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by clouder on 12/8/16.
 */
public class BackpressureDemo
{
    @Test
    public void testBackpressureIssue1()
    {
        Consumer<Object> consumer = v -> System.out
                .println("[" + System.currentTimeMillis() / 100 + "] " + v);
        Flowable<Long> f1 = Flowable.interval(100, TimeUnit.MILLISECONDS);
        Flowable<Long> f2 = Flowable.interval(200, TimeUnit.MILLISECONDS);

        ConnectableFlowable<Long> f3 = Flowable.zip(f1, f2, (x, y) -> x * 10000 + y).publish();

        f3.publish();
        f3.doOnSubscribe(s -> s.request(2)).subscribe(consumer);

        try {
            TimeUnit.SECONDS.sleep(50);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBackpressureIssueBuffer2()
    {
        java.util.function.Function<String, Consumer<Object>> m = s -> v -> System.out
                .println("[" + System.currentTimeMillis() / 100 + "] " + s + "-" + v);

        Flowable<Long> f1 = Flowable.interval(100, TimeUnit.MILLISECONDS)
                .buffer(200, TimeUnit.MILLISECONDS).map(x -> x.get(0)).doOnNext(m.apply("f1"));
        Flowable<Long> f2 = Flowable.interval(200, TimeUnit.MILLISECONDS).doOnNext(m.apply("f2"));

        Flowable<Long> f3 = Flowable.zip(f1, f2, (x, y) -> x * 10000 + y).doOnNext(m.apply("f3"));


        f3.subscribe(m.apply("consumer"), Functions.ERROR_CONSUMER,
                Functions.EMPTY_ACTION);

        try {
            TimeUnit.SECONDS.sleep(50);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBackpressureIssueBlocking()
    {
        java.util.function.Function<String, Consumer<Object>> m = s -> v -> System.out
                .println("[" + System.currentTimeMillis() / 100 + "] " + s + "-" + v);

        Flowable<Long> f1 = Flowable.interval(100, TimeUnit.MILLISECONDS, Schedulers.single())
                .onBackpressureLatest()
                .doOnNext(m
                        .apply("f1"));
        Flowable<Long> f2 = Flowable.interval(200, TimeUnit.MILLISECONDS, Schedulers.single())
                .doOnNext(m.apply("f2"));

        Flowable<Long> f3 = Flowable.zip(f1, f2, (x, y) -> x * 10000 + y).doOnNext(m.apply("f3"));


        f3.blockingSubscribe(m.apply("out"), Functions.ERROR_CONSUMER,
                Functions.EMPTY_ACTION);

        try {
            TimeUnit.SECONDS.sleep(50);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBackpressureIssue()
    {
        java.util.function.Function<String, Consumer<Object>> m = s -> v -> System.out
                .println("[" + System.currentTimeMillis() / 100 + "] " + s + "-" + v);

        Flowable<Long> f1 = Flowable.interval(100, TimeUnit.MILLISECONDS)
                .sample(200, TimeUnit.MILLISECONDS).doOnNext(m.apply("f1"));
        Flowable<Long> f2 = Flowable.interval(200, TimeUnit.MILLISECONDS).doOnNext(m.apply("f2"));

        Flowable<Long> f3 = Flowable.zip(f1, f2, (x, y) -> x * 10000 + y).doOnNext(m.apply("f3"));

        //f3.subscribe(consumer);
//        Consumer<? super Subscription> xxx = x -> {
//            x.request(1);
//
//        };
        f3.subscribe(m.apply("consumer"), Functions.ERROR_CONSUMER,
                Functions.EMPTY_ACTION);

        try {
            TimeUnit.SECONDS.sleep(50);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSpscArrayQueue()
    {
        SpscArrayQueue<String> queue = new SpscArrayQueue<>(3);
        queue.offer("hello");
        queue.offer("hello1");
        System.out.println(queue.offer("hello2"));
        System.out.println(queue.offer("hello3"));
        queue.offer("hello4");
        queue.offer("hello5");
        queue.offer("hello6");
        System.out.println(queue.offer("hello7"));

        System.out.println(queue.length());

        SpscLinkedArrayQueue<Object> linkedQueue = new SpscLinkedArrayQueue<>(3);
        System.out.println(linkedQueue.offer("world"));
        System.out.println(linkedQueue.offer("world1"));
        System.out.println(linkedQueue.offer("world2"));
        System.out.println(linkedQueue.offer("world3"));
        System.out.println(linkedQueue.offer("world4"));
        System.out.println(linkedQueue.size());
    }

    @Test
    public void testBackpressure2() throws InterruptedException
    {
        // Flowable.interval(1, TimeUnit.SECONDS);

        while (true) {
            Point point = MouseInfo.getPointerInfo().getLocation();
            System.out.println(point);
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
            }
        }
    }


    @Test
    public void testBackpressure1() throws InterruptedException
    {
        int scale = 10;
        Subscriber<Object> subscriber = new Subscriber<Object>()
        {
            @Override
            public void onSubscribe(Subscription s)
            {
                s.request(8);
                System.out.println();
                System.out.println();
                s.request(5);
                s.request(15);
            }

            @Override
            public void onNext(Object aLong)
            {
//                try {
//                    TimeUnit.MILLISECONDS.sleep(500);
//                }
//                catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                System.out.println("onNext: " + aLong);
            }

            @Override
            public void onError(Throwable t)
            {
                System.out.println("throwable!!!!");
                t.printStackTrace();
            }

            @Override
            public void onComplete()
            {
                System.out.println("onComplete");
            }
        };
        Flowable.interval(1000, TimeUnit.MILLISECONDS).flatMap(x -> {
            return Flowable.rangeLong(x * scale + 1, x * scale + scale);
        }).onBackpressureLatest().subscribe(subscriber);

        TimeUnit.SECONDS.sleep(20);
    }

    @Test
    public void testbackpressure() throws InterruptedException
    {

        Consumer<Object> consumer = x -> System.out
                .println("Thread[" + Thread.currentThread().getName() + " ," + Thread
                        .currentThread().getId() + "] :" + x);


        Flowable<String> f1 = Flowable.intervalRange(1, 100, 1, 1, TimeUnit.MILLISECONDS)

                .onBackpressureLatest()

                .zipWith(Flowable.interval(1000, TimeUnit.MILLISECONDS), (x, y) -> x + "-" + y);


        Disposable d = f1.observeOn(Schedulers.newThread()).map(x -> {
            System.out.println(x);
            TimeUnit.MILLISECONDS.sleep(700);
            return x + 1;
        }).take(20).subscribe(consumer, consumer);

        while (!d.isDisposed()) {

        }
        //TimeUnit.SECONDS.sleep(20);
    }
}
