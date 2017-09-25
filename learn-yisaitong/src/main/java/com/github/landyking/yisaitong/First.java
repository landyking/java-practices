package com.github.landyking.yisaitong;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 环境：可以正常解密的环境<br/>
 * 流程：<br/>
 * 1.复制java.exe为EXCEL.exe（WINWORD.EXE）<br/>
 * 2.当前目录下打开命令行：./WINWORD.EXE -jar ${this}.jar *.docx 或 ./EXCEL.EXE -jar ${this}.jar *.xls<br/>
 *
 * @author: Landy
 * @date: 2017/8/25 11:09
 * note:
 */
public class First {

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            String input = args[0];
            File inputFile = new File(input);
            System.out.println("input: " + inputFile.getAbsolutePath());
            String output = UUID.randomUUID().toString().replaceAll("-", "");
            File outputFile = new File(inputFile.getParent(), output);
            Files.copy(inputFile, outputFile);
            System.out.println("output: " + outputFile.getAbsolutePath());
        } else {
            System.out.println("need one argument!");
        }
    }
}
