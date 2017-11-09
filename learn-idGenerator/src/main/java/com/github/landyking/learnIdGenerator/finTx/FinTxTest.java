package com.github.landyking.learnIdGenerator.finTx;

import org.fintx.util.UniqueId;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/11/9 13:46
 * note:
 */
public class FinTxTest {

    public static void main(String[] args) {
        UniqueId uniqueId = UniqueId.get();
        System.out.println(uniqueId.toBase64String());
        System.out.println(uniqueId.toString());
        System.out.println(uniqueId.toHexString());
    }
}
