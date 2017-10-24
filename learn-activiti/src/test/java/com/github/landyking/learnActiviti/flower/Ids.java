package com.github.landyking.learnActiviti.flower;

import java.util.UUID;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/24 16:54
 * note:
 */
public class Ids {
    public static String newID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
