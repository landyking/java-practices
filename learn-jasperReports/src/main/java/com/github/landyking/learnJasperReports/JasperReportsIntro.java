package com.github.landyking.learnJasperReports;

import net.sf.jasperreports.engine.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class JasperReportsIntro {
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