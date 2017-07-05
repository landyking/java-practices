package com.github.landyking.learnActiviti;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description：TODO <br/>
 *
 * @author: 黄地
 * @date: 2017/7/5 11:04
 * note:
 */
public class Tools {
    public static String longFmt(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String longFmt(long time) {
        return longFmt(new Date(time));
    }
}
