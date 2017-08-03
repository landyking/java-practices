package com.github.landyking.learnJasperReports;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/8/3 11:47
 * note:
 */
public class Utils {
    public static File getTargetDir() throws URISyntaxException {
        URL resource = Utils.class.getResource("/");
        File classDir = new File(resource.toURI());
        File targetDir = new File(classDir, "..");
        return targetDir;
    }

    public static InputStream getResourceAsStream(String name) {
        return Utils.class.getResourceAsStream(name);
    }

    public static URL getResource(String name) {
        return Utils.class.getResource(name);
    }
}
