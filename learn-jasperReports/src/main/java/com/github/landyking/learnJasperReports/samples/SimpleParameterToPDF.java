package com.github.landyking.learnJasperReports.samples;

import com.github.landyking.learnJasperReports.JasperMaster;
import net.sf.jasperreports.engine.*;

import java.util.HashMap;

/**
 * Description：
 * <ol>
 * <li>一个简单的模板。上面展示三句话。这三句话需要传入值才能显示</li>
 * <li>传入一句字母，两句汉语给报表引擎</li>
 * <li>生成pdf</li>
 * <li>检查确认生成成功，且没有乱码</li>
 * </ol>
 *
 * @author: Landy
 * @date: 2017/8/3 11:37
 * note:
 */
public class SimpleParameterToPDF {
    public static void main(String[] args) {
        new JasperMaster("/jrxml/samples/simpleParameter.jrxml", "simpleParameter.pdf") {

            @Override
            public JasperPrint fillReport(JasperReport jasperReport) throws JRException {
                HashMap parameters = new HashMap();
                parameters.put("param1", "first");
                parameters.put("param2", "second");
                parameters.put("param3", "我是个好人");
                return JasperFillManager.fillReport(
                        jasperReport, parameters, new JREmptyDataSource());
            }
        }.doWork();
    }
}
