package com.github.landyking.learnSamba.server;

import org.alfresco.jlan.netbios.server.NetBIOSNameServer;
import org.alfresco.jlan.server.NetworkServer;
import org.alfresco.jlan.server.config.InvalidConfigurationException;
import org.alfresco.jlan.server.config.ServerConfiguration;
import org.alfresco.jlan.server.core.DeviceContextException;
import org.alfresco.jlan.smb.server.SMBServer;

import java.io.IOException;

/**
 * Description：<br/>
 *
 * @author: landy
 * @date: 2016/09/15 16:59
 * note:
 */
public class Main {
    public static void main(String[] args) throws InvalidConfigurationException, DeviceContextException, IOException {
        ServerConfiguration cfg = new JLANFileServerConfiguration();

        NetBIOSNameServer netBIOSNameServer = new NetBIOSNameServer(cfg);
        cfg.addServer(netBIOSNameServer);
        SMBServer smbServer = new SMBServer(cfg);
        cfg.addServer(smbServer);

        // start servers
        for (int i = 0; i < cfg.numberOfServers(); i++) {
            NetworkServer server = cfg.getServer(i);
            server.startServer();
            System.out.println("start server: "+server.toString());
        }
        System.out.println("************* start success ***************");
    }

}
