package com.github.landyking.learnJasperReports.samples;

import com.github.landyking.learnJasperReports.JasperMaster;
import com.github.landyking.learnJasperReports.Utils;
import net.sf.jasperreports.engine.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/8/4 15:49
 * note:
 */
public class SimpleSubreportToPDF {
    public static void main(String[] args) throws IOException {
        JasperMaster help = new JasperMaster("/jrxml/samples/simpleSubreport_master.jrxml", "simpleSubreport.pdf") {

            @Override
            public JasperPrint fillReport(JasperReport jasperReport) throws JRException {
                HashMap parameters = new HashMap();
                String path = Utils.getResource("/jrxml/samples/simpleSubreport_subreport.jrxml").getPath();
                System.out.println("subreport path: " + path);
                String subJasperFile = JasperCompileManager.compileReportToFile(path);
                System.out.println("compile subreport: " + subJasperFile);
                parameters.put("subname", subJasperFile);
                parameters.put("sub_txt", "sub txt 11");
                HashMap subparams = new HashMap();
                subparams.put("sub_txt", "sub txt 22");
                parameters.put("subparams", subparams);
                return JasperFillManager.fillReport(
                        jasperReport, parameters, new JREmptyDataSource());
            }
        };
        help.doWork();
        help.showOutputFile();
    }
}
