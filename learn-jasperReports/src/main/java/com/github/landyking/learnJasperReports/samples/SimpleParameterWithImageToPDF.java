package com.github.landyking.learnJasperReports.samples;

import com.github.landyking.learnJasperReports.JasperMaster;
import com.github.landyking.learnJasperReports.Utils;
import net.sf.jasperreports.engine.*;

import java.util.HashMap;

/**
 * Description：
 * <ol>
 * <li>一个简单的模板。上面展示三句话，和两副图片。</li>
 * <li>传入一句字母，两句汉语给报表引擎</li>
 * <li>传入一个图片地址，一个二进制图片数据给报表引擎</li>
 * <li>生成pdf</li>
 * <li>检查确认生成成功，文本正常且没有乱码。两幅图片均正常显示</li>
 * </ol>
 *
 * @author: Landy
 * @date: 2017/8/3 11:37
 * note:
 */
public class SimpleParameterWithImageToPDF {
    public static void main(String[] args) {
        new JasperMaster("/jrxml/samples/simpleParameterWithImage.jrxml", "simple_report_img.pdf") {

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
//                parameters.put("img2", Utils.getResourceAsStream(name));
                return JasperFillManager.fillReport(
                        jasperReport, parameters, new JREmptyDataSource());
            }
        }.doWork();
    }
}
