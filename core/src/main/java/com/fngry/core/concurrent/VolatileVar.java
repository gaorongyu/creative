package com.fngry.core.concurrent;

/**
 * Created by gaorongyu on 15/7/14.
 */
public class VolatileVar {

    private volatile boolean isExit = false;

    public void tryExit() {
        if(isExit == !isExit) {
            System.out.println(" Goodbye !!! ");
            System.exit(0);
        }
    }

    public void swapIsExit() {
        this.isExit = !this.isExit;
    }

    public static void main(String[] args) throws Exception {
        final VolatileVar obj = new VolatileVar();

        Thread mainThread = new Thread(
            new Runnable() {
                public void run() {
                    System.out.println(" main thread start ");
                    while(true) {
                        obj.tryExit();
                    }
                }
            }
        );
        mainThread.start();

        Thread swapThread = new Thread(
            new Runnable() {
                public void run() {
                    System.out.println(" swap thread start ");
                    while(true) {
                        obj.swapIsExit();
                    }
                }
            }
        );
        swapThread.start();

    }

}
