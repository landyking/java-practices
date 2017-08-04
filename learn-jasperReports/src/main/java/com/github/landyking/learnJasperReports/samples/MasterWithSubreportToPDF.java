package com.github.landyking.learnJasperReports.samples;

import com.github.landyking.learnJasperReports.JasperMaster;
import com.github.landyking.learnJasperReports.Utils;
import net.sf.jasperreports.engine.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Description：
 * <ol>
 * <li>一个简单主模块，一个文字占位，一个图片占位，一个子报表占位</li>
 * <li>子报表中包含一个文字占位，一个图片占位</li>
 * <li>传入变量：文字，图片二进制数据，子报表名称，子报表的文字图片</li>
 * <li>生成pdf</li>
 * <li>检查确认生成成功，且没有乱码</li>
 * </ol>
 *
 * @author: Landy
 * @date: 2017/8/4 10:08
 * note:
 */
public class MasterWithSubreportToPDF {
    public static void main(String[] args) throws IOException {
        JasperMaster help = new JasperMaster("/jrxml/samples/masterWithSubreport.jrxml", "masterWithSubreport.pdf") {

            @Override
            public JasperPrint fillReport(JasperReport jasperReport) throws JRException {
                HashMap parameters = new HashMap();
                parameters.put("param1", "Hello World");
                parameters.put("param2", "李元霸");
                parameters.put("param3", "从前有座山，山里有座庙，庙里有个老和尚在讲故事。");
                String name = "/jrxml/samples/girl_head.jpg";
                String file = Utils.getResource(name).getFile();
                System.out.println("img1: " + file);
                parameters.put("img1", file);
                parameters.put("img2", Utils.getResourceAsStream(name));
                return JasperFillManager.fillReport(
                        jasperReport, parameters, new JREmptyDataSource());
            }
        };
        help.doWork();
        help.showOutputFile();
    }
}
