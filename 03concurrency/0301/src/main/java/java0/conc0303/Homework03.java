package java0.conc0303;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 一个简单的代码参考：
 */
public class Homework03 {
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        
        long start=System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        // 1. Future
//        ExecutorService threadPool = Executors.newFixedThreadPool(1);
//        Future<Integer> result = threadPool.submit(() -> sum());
//        // 确保  拿到result 并输出
//        System.out.println("异步计算结果为："+result.get());
//        threadPool.shutdown();
//
//        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 2. FutureTask
//        ExecutorService threadPool = Executors.newFixedThreadPool(1);
//        FutureTask<Integer> futureTask = new FutureTask<>(() -> sum());
//        threadPool.execute(futureTask);
//        System.out.println("异步计算结果为："+ futureTask.get());
//        threadPool.shutdown();
//
//        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 3. CompletableFuture
//        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> sum());
//        System.out.println("异步计算结果为："+ completableFuture.join());
//        System.out.println("异步计算结果为："+ completableFuture.get());
//        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // or
//        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> sum()).thenAccept(result -> {
//            System.out.println("异步计算结果为："+ result);
//            System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
//        });
//        completableFuture.join();


        // 4. AtomicInteger
//        AtomicInteger result = new AtomicInteger();
//        Thread t = new Thread(() -> result.set(sum()));
//        t.start();
//
//        for (;;) {
//            if (result.get() != null) break;
//        }
//
//        System.out.println("异步计算结果为："+ result);
//        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 5. CountDownLatch
//        CountDownLatch latch = new CountDownLatch(1);
//        AtomicInteger result = new AtomicInteger();
//        Thread t = new Thread(() -> {
//            result.set(sum());
//            latch.countDown();
//        });
//        t.start();
//
//        latch.await();
//        // or 与t.wait()相同
//        t.join();
//
//        System.out.println("异步计算结果为："+ result.get());
//        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 6. park & unpark

        Thread currentThread = Thread.currentThread();
        AtomicInteger result = new AtomicInteger();
        Thread t = new Thread(() -> {
            result.set(sum());
            LockSupport.unpark(currentThread);
        });
        t.start();

        LockSupport.park();

        System.out.println("异步计算结果为："+ result.get());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }
    
    private static int sum() {
        return fibo(36);
    }
    
    private static int fibo(int a) {
        if ( a < 2) 
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
