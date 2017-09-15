package com.github.landyking.learnConcurrency;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * Description：展示 reject 什么时间有效 <br/>
 *
 * @author: Landy
 * @date: 2017/9/15 11:04
 * note:
 */
public class RejectMainTest {
    @Test
    public void test111() throws Exception {

        ExecutorService executor = new ThreadPoolExecutor(1, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
        int i = 120;
        for (int i1 = 0; i1 < i; i1++) {
            final int finalI = i1;
            try {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("begin " + finalI);
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("end " + finalI);
                    }
                });
                System.out.println("execute " + finalI);
            } catch (RejectedExecutionException e) {
                System.out.println("rejected " + finalI);
            }
        }
        executor.shutdownNow();
    }

    @Test
    public void test222() throws Exception {
        ExecutorService executor = new ThreadPoolExecutor(1, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(10));
        int i = 120;
        for (int i1 = 0; i1 < i; i1++) {
            final int finalI = i1;
            try {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("begin " + finalI);
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("end " + finalI);
                    }
                });
                System.out.println("execute " + finalI);
            } catch (RejectedExecutionException e) {
                System.out.println("rejected " + finalI);
            }
        }
        executor.shutdownNow();
    }
}