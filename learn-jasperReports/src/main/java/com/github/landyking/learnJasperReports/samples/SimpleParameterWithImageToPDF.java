package com.github.landyking.learnJasperReports.samples;

import com.github.landyking.learnJasperReports.JasperMaster;
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
        new JasperMaster("/jrxml/jasperreports_demo.jrxml", "simple_report.pdf") {

            @Override
            public JasperPrint fillReport(JasperReport jasperReport) throws JRException {
                return JasperFillManager.fillReport(
                        jasperReport, new HashMap(), new JREmptyDataSource());
            }
        }.doWork();
    }
}
