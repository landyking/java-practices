package com.github.landyking.learnFastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;

import java.io.IOException;

/**
 * Created by landy on 2017/11/21.
 */
public class Main {
    public static void main(String[] args) throws IOException, MyException {
        ClientGlobal.init(Main.class.getResource("/fdfs_client.conf").getFile());
        System.out.println("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());
    }
}
