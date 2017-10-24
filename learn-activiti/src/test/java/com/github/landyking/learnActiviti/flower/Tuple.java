package com.github.landyking.learnActiviti.flower;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/24 19:53
 * note:
 */
public class Tuple<A, B> {
    private final A first;
    private final B second;

    private Tuple(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public static <A, B> Tuple<A, B> newInstance(A first, B second) {
        return new Tuple<A, B>(first, second);
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}
