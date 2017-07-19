package com.github.landyking.learnActiviti.spring;

public class Printer {
    private int mark = 0;

    public int getMark() {
        return mark;
    }

    public void printMessage() {
        System.out.println("hello world");
        mark = 2;
    }
}