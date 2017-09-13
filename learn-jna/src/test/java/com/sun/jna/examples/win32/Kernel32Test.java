package com.sun.jna.examples.win32;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/9/12 15:03
 * note:
 */
public class Kernel32Test {
    @Test
    public void getSystemTime() throws Exception {
        Kernel32 lib = Kernel32.INSTANCE;
        Kernel32.SYSTEMTIME time = new Kernel32.SYSTEMTIME();
        System.out.println(time.toString());
        lib.GetSystemTime(time);
        System.out.println(time.toString());
        System.out.println("Today's integer value is " + time.wDay);
    }

}