package com.github.landyking.learnJasperReports.issueDocument;

import com.github.landyking.learnJasperReports.JasperMaster;
import com.github.landyking.learnJasperReports.Utils;
import net.sf.jasperreports.engine.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/8/7 15:39
 * note:
 */
public class IssueDocumentTest {
    public static void main(String[] args) throws IOException {
        JasperMaster help = new JasperMaster("/jrxml/issueDocument/issueDocumentTest.jrxml", "issueDocument.pdf") {

            @Override
            public JasperPrint fillReport(JasperReport jasperReport) throws JRException {
//                String sub_qianfa_name = compileReport("/jrxml/issueDocument/sub_qianfa_1.jrxml");
//                String sub_huiqian_name = compileReport("/jrxml/issueDocument/sub_huiqian_1.jrxml");
//                String sub_bangongshishenhe_name = compileReport("/jrxml/issueDocument/sub_bangongshishenhe_.jrxml");
//                String sub_nigaoren_name = compileReport("/jrxml/issueDocument/sub_nigaoren_1.jrxml");

                String sub_qianfa_name = compileReport("/jrxml/issueDocument/sub_qianfa_2.jrxml");
//                String sub_huiqian_name = compileReport("/jrxml/issueDocument/sub_huiqian_2.jrxml");
                String sub_huiqian_name = compileReport("/jrxml/issueDocument/sub_huiqian_huiyijiyao.jrxml");
                String sub_bangongshishenhe_name = compileReport("/jrxml/issueDocument/sub_bangongshishenhe_2.jrxml");
                String sub_nigaoren_name = compileReport("/jrxml/issueDocument/sub_nigaoren_1.jrxml");
                HashMap rootParams = new HashMap();
                fill_qianfa(sub_qianfa_name, rootParams);
                fill_huiqian(sub_huiqian_name, rootParams);
                fill_bangongshishenhe(sub_bangongshishenhe_name, rootParams);
                fill_nigaoren(sub_nigaoren_name, rootParams);
                return JasperFillManager.fillReport(
                        jasperReport, rootParams, new JREmptyDataSource());
            }
        };
        help.doWork();
        help.showOutputFile();
    }

    private static void fill_nigaoren(String sub_nigaoren_name, HashMap rootParams) {
        HashMap params = new HashMap();
        params.put("txt1", "吃葡萄不吐葡萄皮，不吃葡萄到吐葡萄皮。");
        params.put("txt2", "2吃葡萄不吐葡萄皮，不吃葡萄到吐葡萄皮。");
        params.put("sign1", Utils.getResource("/jrxml/issueDocument/sign.png").getPath());
        params.put("sign2", Utils.getResource("/jrxml/issueDocument/sign.png").getPath());
        rootParams.put("sub_nigaoren_params", params);
        rootParams.put("sub_nigaoren_name", sub_nigaoren_name);
    }

    private static void fill_bangongshishenhe(String sub_bangongshishenhe_name, HashMap rootParams) {
        HashMap params = new HashMap();
        params.put("txt1", "从前有座山，山里有座庙，庙里有个老和尚在给小和尚讲故事。");
        params.put("txt2", "2从前有座山，山里有座庙，庙里有个老和尚在给小和尚讲故事。");
        params.put("sign1", Utils.getResource("/jrxml/issueDocument/sign.png").getPath());
        params.put("sign2", Utils.getResource("/jrxml/issueDocument/sign.png").getPath());
        rootParams.put("sub_bangongshishenhe_params", params);
        rootParams.put("sub_bangongshishenhe_name", sub_bangongshishenhe_name);
    }

    private static void fill_huiqian(String sub_huiqian_name, HashMap rootParams) {
        HashMap params = new HashMap();
        for (int i = 1; i <= 12; i++) {
            params.put("sign" + i,null);
        }
        params.put("txt1", "打南边来了一个白胡子喇嘛，手里提着二斤鳎犸。");
        params.put("txt2", "2打南边来了一个白胡子喇嘛，手里提着二斤鳎犸。");
        params.put("sign1", Utils.getResource("/jrxml/issueDocument/sign.png").getPath());
        params.put("sign2", Utils.getResource("/jrxml/issueDocument/sign.png").getPath());
        params.put("sign3", Utils.getResource("/jrxml/issueDocument/sign.png").getPath());
        params.put("sign4", Utils.getResource("/jrxml/issueDocument/sign.png").getPath());
        rootParams.put("sub_huiqian_params", params);
        rootParams.put("sub_huiqian_name", sub_huiqian_name);
    }

    private static void fill_qianfa(String sub_qianfa_name, HashMap rootParams) {
        HashMap params = new HashMap();
        params.put("txt1", "千山鸟飞绝，万径人踪灭。孤舟蓑笠翁，独钓寒江雪。");
        params.put("txt2", "2千山鸟飞绝，万径人踪灭。孤舟蓑笠翁，独钓寒江雪。");
        params.put("sign1", Utils.getResource("/jrxml/issueDocument/sign.png").getPath());
        params.put("sign2", Utils.getResource("/jrxml/issueDocument/sign.png").getPath());
        rootParams.put("sub_qianfa_params", params);
        rootParams.put("sub_qianfa_name", sub_qianfa_name);
    }
}
