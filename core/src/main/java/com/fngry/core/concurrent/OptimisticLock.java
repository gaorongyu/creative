package com.fngry.core.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by gaorongyu on 15/7/16.
 */
public class OptimisticLock {

    final CyclicBarrier barrier = new CyclicBarrier(2, new Runnable() {
        public void run() {
            System.out.println(" all arrivaled " + Thread.currentThread().getName() + " at " + System.nanoTime());
        }
    });

    private final AtomicLong count = new AtomicLong(0);

    private Long commonCount = new Long(0);


    public void increaseCommon() {
        commonCount++;
    }

    public long getCommonCount() {
        return this.commonCount;
    }

    public void increase() {
        boolean updated = false;
        while(!updated) {
            long currentValue = this.count.get();
            updated = this.count.compareAndSet(currentValue, currentValue + 1);
        }
    }

    public long getCount() {
        return this.count.get();
    }

    public static void main(String[] args) throws Exception {

        final OptimisticLock optimisticLock = new OptimisticLock();

        optimisticLock.test();
//
//        Runnable task = () -> {
//            for(int i = 0; i < 100000; i++) {
////                optimisticLock.increase();
//                optimisticLock.increaseCommon();
//            }
//        };
//        task.run();
//
//        Runnable task1 = () -> {
//            for(int i = 0; i < 100000; i++) {
////                optimisticLock.increase();
//                optimisticLock.increaseCommon();
//
//            }
//        };
//        System.out.println(optimisticLock.getCount());
        System.out.println( "common " + optimisticLock.getCommonCount());
        System.out.println( "atomic " + optimisticLock.getCount());


    }

    public void test() throws  Exception {
        Thread task = new Thread(new Worker());
        task.start();
        Thread task1 = new Thread(new Worker());
        task1.start();

        task.join();
        task1.join();
    }

    class Worker implements Runnable {
        @Override
        public void run() {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println( " thread " + Thread.currentThread().getName() + " at " + System.nanoTime());

            for(int i = 0; i < 10000000; i++) {
                increaseCommon();
                increase();
            }
        }
    }

}
