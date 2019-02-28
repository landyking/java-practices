package com.github.landyking.learnConcurrency;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * Created by landy on 2019/2/28.
 */
public class CyclicBarrierTest {
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        final CyclicBarrier barrier = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " # barrier action");
            }
        });
        for (int i = 0; i < 4; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 2; j++) {

                        try {
                            System.out.println(Thread.currentThread().getName() + "-" + j + " ##### start");
                            int await = barrier.await();
                            System.out.println(Thread.currentThread().getName() + "-" + j + " ##### over "+await);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        System.out.println("############");
        TimeUnit.SECONDS.sleep(100);
    }
}
