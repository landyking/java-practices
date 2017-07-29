package com.github.landyking.learnJasperReports;

import net.sf.jasperreports.engine.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class JasperReportsIntro {
    public static void main(String[] args) {
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        try {
            URL resource = JasperReportsIntro.class.getResource("/");
            File classDir = new File(resource.toURI());
            File targetDir = new File(classDir, "..");
            System.out.println("target: "+targetDir.getAbsolutePath());
            File jasperReportsDir = new File(targetDir, "jasperReports");
            jasperReportsDir.mkdirs();
            jasperReport = JasperCompileManager.compileReport(JasperReportsIntro.class.getResourceAsStream("/jrxml/jasperreports_demo.jrxml"));
            jasperPrint = JasperFillManager.fillReport(
                    jasperReport, new HashMap(), new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(
                    jasperPrint, new File(jasperReportsDir, "simple_report.pdf").getAbsolutePath());
            JasperExportManager.exportReportToHtmlFile(jasperPrint, new File(jasperReportsDir, "simple_report.html").getAbsolutePath());
        } catch (JRException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}