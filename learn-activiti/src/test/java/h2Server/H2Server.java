package h2Server;

import org.h2.tools.Server;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/23 13:42
 * note:
 */
public class H2Server {
    public static void main(String[] args) throws SQLException, URISyntaxException {
        URL resource = H2Server.class.getResource("/");
        File file = new File(resource.toURI());
        if (file.exists()) {
            file = new File(file.getParentFile().getParentFile(), "h2Home");
            file.mkdir();
        }
        System.out.println(file.getAbsolutePath());
        Server tcpServer = Server.createTcpServer(new String[]{"-baseDir", file.getAbsolutePath()});
        Server server = tcpServer.start();
// stop the TCP Server

    }
}
