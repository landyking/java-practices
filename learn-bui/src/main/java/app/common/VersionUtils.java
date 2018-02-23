package app.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *  <br/>
 *
 * @author: 黄地
 * @date: 2017/2/8 10:20
 * note:
 */
public class VersionUtils {
    public static final String APP_VERSION;

    private static final String VERSION_PROPERTIES_FILE = "/version.properties";

    static {
        Properties prop = new Properties();
        InputStream stream = VersionUtils.class.getResourceAsStream(VERSION_PROPERTIES_FILE);
        if (stream != null) {
            try {
                prop.load(stream);
            } catch (IOException e) {
                System.out.println("properties load failure : classpath:" + VERSION_PROPERTIES_FILE);
            }
        } else {
            System.out.println("properties file not found : classpath:" + VERSION_PROPERTIES_FILE);
        }
        APP_VERSION = prop.getProperty("app.version", "unknown");
        System.out.println("properties load [app.version=" + APP_VERSION + "]");
    }

    public static void init() {

    }
}
