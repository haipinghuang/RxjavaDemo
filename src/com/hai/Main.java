package com.hai;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class Main {
    public static void main(String[] args) {

        Flowable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000); //  imitate expensive computation
                return "Done";
            }
        })/*.subscribeOn(Schedulers.io())*/
                .observeOn(Schedulers.single())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        System.out.println(Thread.currentThread().getName());
                    }
                });
        try {
            Thread.sleep(2000); // <--- wait for the flow to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*********************************************************/
    void transform() {

        /**
         * 毫无转换的直接订阅、发布
         */
        Flowable.just("Hello world").subscribe(new Consumer<String>() {
            @Override
            public void accept(String x) throws Throwable {
                int a = 3 / 0;
                System.out.println(x);
            }
        });
        /**
         *订阅后，数据源一个一个的经过各个transformer传给订阅者
         */
        Flowable<Integer> flow = Flowable.range(1, 5)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer v) throws Throwable {
                        return v * v;
                    }
                })
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer v) throws Throwable {
                        return v % 3 == 0;
                    }
                });
        flow.subscribe(System.out::println);
        /**
         *跟上一个差不多，一个分组转换
         * buffer这个Flowable会累积上层发过来的item，到了buffer个后再往下层发送
         */
        Flowable.fromArray(2, 35, 7, 23, 7, 83, 234, 4, 7, 2)
                .buffer(3)//累积到三个一组发送
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Throwable {
                        System.out.println(integers.size());
                    }
                });
        /**
         * 一对多转换
         */
        Flowable.fromArray(2, 35, 7, 23)
                .flatMap(new Function<Integer, Publisher<String>>() {

                    @Override
                    public Publisher<String> apply(Integer integer) throws Throwable {
                        //一对多转换
                        return Flowable.fromArray("a", "b", "c");
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Throwable {
                System.out.println(s);
            }
        });

    }

}