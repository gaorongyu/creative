package com.fngry.core.concurrent;

import java.util.concurrent.Semaphore;

/**
 *
 * 信号量
 *
 * Created by gaorongyu on 15/7/15.
 *
 */
public class Pool {

    private static final int MAX_AVAILABLE = 10;

    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

    protected Object[] items = new Integer[MAX_AVAILABLE];

    protected boolean[] used = new boolean[MAX_AVAILABLE];


    public static void main(String[] args) throws Exception {

        final Pool pool = new Pool();

        pool.initItems();

        // 没有许可时，阻塞
        new Thread(new Runnable() {
            public void run() {
                for(int i = 0; i < MAX_AVAILABLE + 1; i++) {
                    Object obj = null;
                    System.out.println(" semaphore permits is " + pool.getAvailable().availablePermits());
                    try {
                        obj = pool.getItem();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(" get item is " + obj);
                }
            }
        }).start();

        // 释放一个许可，上一个线程获取到继续运行
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pool.putItem(8);
                System.out.println(" semaphore permits after put is " + pool.getAvailable().availablePermits());
            }
        }).start();

    }

    public Semaphore getAvailable() {
        return this.available;
    }

    public void initItems() {
        for(int i = 0; i < MAX_AVAILABLE; i++) {
            items[i] = i;
        }
    }

    public Object getItem() throws InterruptedException {
        available.acquire();
        return getNextAvailableItem();
    }

    public void putItem(Object item) {
        if(markAsUnused(item)) {
            available.release();
        }
    }

    public synchronized Object getNextAvailableItem() {
        for(int i = 0; i < MAX_AVAILABLE; ++i) {
            if(!used[i]) {
                used[i] = true;
                return items[i];
            }
        }
        return null;
    }

    protected synchronized boolean markAsUnused(Object item) {
        for(int i = 0; i < MAX_AVAILABLE; ++i) {
            if(item == items[i]) {
                if (used[i]) {
                    used[i] = false;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

}
