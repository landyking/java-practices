package com.github.landyking.learnIdGenerator.snowflake.downgoon;

import xyz.downgoon.snowflake.Snowflake;

import java.util.concurrent.TimeUnit;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/11/9 14:05
 * note:
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {

        Snowflake snowflake = new Snowflake(2, 5);
        long id1 = snowflake.nextId();
        long id2 = snowflake.nextId();

        snowflake.formatId(id1);
        System.out.println(id1);
        System.out.println(id2);
        System.out.println("************");
        System.out.println(GUIDUtil.nextId());
        System.out.println(GUIDUtil.nextId());
        System.out.println(GUIDUtil.nextId());
        System.out.println(GUIDUtil.nextId());
        TimeUnit.SECONDS.sleep(1);
        System.out.println("************");
        System.out.println(GUIDUtil.nextId());
        System.out.println(GUIDUtil.nextId());
        System.out.println(GUIDUtil.nextId());
        System.out.println(GUIDUtil.nextId());
    }
}
