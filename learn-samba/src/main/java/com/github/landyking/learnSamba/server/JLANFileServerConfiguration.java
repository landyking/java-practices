package com.github.landyking.learnSamba.server;

import org.alfresco.jlan.debug.DebugConfigSection;
import org.alfresco.jlan.server.SrvSession;
import org.alfresco.jlan.server.auth.CifsAuthenticator;
import org.alfresco.jlan.server.auth.ClientInfo;
import org.alfresco.jlan.server.auth.LocalAuthenticator;
import org.alfresco.jlan.server.auth.UserAccountList;
import org.alfresco.jlan.server.auth.acl.DefaultAccessControlManager;
import org.alfresco.jlan.server.config.*;
import org.alfresco.jlan.server.core.DeviceContextException;
import org.alfresco.jlan.server.core.SharedDevice;
import org.alfresco.jlan.server.core.SharedDeviceList;
import org.alfresco.jlan.server.filesys.*;
import org.alfresco.jlan.server.thread.ThreadRequestPool;
import org.alfresco.jlan.smb.server.CIFSConfigSection;
import org.springframework.extensions.config.element.GenericConfigElement;

import java.util.Enumeration;

/**
 * Description：<br/>
 *
 * @author: landy
 * @date: 2016/09/15 17:03
 * note:
 */
public class JLANFileServerConfiguration extends ServerConfiguration {
    private static final String HOSTNAME = "JLANHOST";

    private static final int DefaultThreadPoolInit = 25;
    private static final int DefaultThreadPoolMax = 50;

    private static final int[] DefaultMemoryPoolBufSizes = {256, 4096, 16384, 66000};
    private static final int[] DefaultMemoryPoolInitAlloc = {20, 20, 5, 5};
    private static final int[] DefaultMemoryPoolMaxAlloc = {100, 50, 50, 50};

    public JLANFileServerConfiguration() throws InvalidConfigurationException, DeviceContextException {
        super(HOSTNAME);
        setServerName(HOSTNAME);

        // DEBUG
        DebugConfigSection debugConfig = new DebugConfigSection(this);
        final GenericConfigElement debugConfigElement = new GenericConfigElement("output");
        final GenericConfigElement logLevelConfigElement = new GenericConfigElement("logLevel");
        logLevelConfigElement.setValue("Debug");
        debugConfig.setDebug("org.alfresco.jlan.debug.ConsoleDebug", debugConfigElement);

        // CORE
        CoreServerConfigSection coreConfig = new CoreServerConfigSection(this);
        coreConfig.setMemoryPool(DefaultMemoryPoolBufSizes, DefaultMemoryPoolInitAlloc, DefaultMemoryPoolMaxAlloc);
        coreConfig.setThreadPool(DefaultThreadPoolInit, DefaultThreadPoolMax);
        ThreadRequestPool threadPool = coreConfig.getThreadPool();
        threadPool.setDebug(true);

        // GLOBAL
        GlobalConfigSection globalConfig = new GlobalConfigSection(this);

        // SECURITY
        SecurityConfigSection secConfig = new SecurityConfigSection(this);
        DefaultAccessControlManager accessControlManager = new DefaultAccessControlManager() {
            @Override
            public SharedDeviceList filterShareList(SrvSession sess, SharedDeviceList shares) {
                SharedDeviceList sharedDeviceList = super.filterShareList(sess, shares);
                Enumeration<SharedDevice> iter = sharedDeviceList.enumerateShares();
                SharedDeviceList rst = new SharedDeviceList();
                while (iter.hasMoreElements()) {
                    SharedDevice sharedDevice = iter.nextElement();
                    //此处不过滤，在华硕路由器上会有问题。列表不显示。
                    if (sharedDevice instanceof DiskSharedDevice) {
                        rst.addShare(sharedDevice);

                    }
                }
                return rst;
            }
        };
        accessControlManager.setDebug(true);
        accessControlManager.initialize(this, new GenericConfigElement("aclManager"));
        secConfig.setAccessControlManager(accessControlManager);
//        secConfig.setJCEProvider("cryptix.jce.provider.CryptixCrypto");
        final UserAccountList userAccounts = new UserAccountList();
        secConfig.setUserAccounts(userAccounts);

        // SHARES
        FilesystemsConfigSection filesysConfig = new FilesystemsConfigSection(this);
        DiskInterface diskInterface = new org.alfresco.jlan.smb.server.disk.JavaFileDiskDriver();
        final GenericConfigElement driverConfig = new GenericConfigElement("driver");
        final GenericConfigElement localPathConfig = new GenericConfigElement("LocalPath");
        localPathConfig.setValue(".");
        driverConfig.addChild(localPathConfig);
        String jlanshare = "SHARE";
        DiskDeviceContext diskDeviceContext = (DiskDeviceContext) diskInterface.createContext(jlanshare, driverConfig);
//        diskDeviceContext.setShareName(jlanshare);
        diskDeviceContext.setConfigurationParameters(driverConfig);
        diskDeviceContext.enableChangeHandler(false);
        diskDeviceContext.setDiskInformation(new SrvDiskInfo(2560000, 64, 512, 2304000));// Default to a 80Gb sized disk with 90% free space
        DiskSharedDevice diskDev = new DiskSharedDevice(jlanshare, diskInterface, diskDeviceContext);
        diskDev.setConfiguration(this);
        diskDev.setAccessControlList(secConfig.getGlobalAccessControls());
        diskDeviceContext.startFilesystem(diskDev);
        filesysConfig.getShares().addShare(diskDev);

        // SMB
        CIFSConfigSection cifsConfig = new CIFSConfigSection(this);
        cifsConfig.setServerName(HOSTNAME);
        cifsConfig.setDomainName("MYDOMAIN");
        cifsConfig.setHostAnnounceInterval(5);
        cifsConfig.setHostAnnouncer(true);
        final CifsAuthenticator authenticator = new LocalAuthenticator() {
            @Override
            public int authenticateUser(ClientInfo client, SrvSession sess, int alg) {
                return AUTH_ALLOW;
            }
        };
        authenticator.setDebug(true);
        authenticator.setAllowGuest(true);
        authenticator.setAccessMode(CifsAuthenticator.SHARE_MODE);
        final GenericConfigElement authenticatorConfigElement = new GenericConfigElement("authenticator");
        authenticator.initialize(this, authenticatorConfigElement);
        cifsConfig.setAuthenticator(authenticator);
        cifsConfig.setHostAnnounceDebug(true);
//        cifsConfig.setNetBIOSDebug(true);
//        cifsConfig.setSessionDebugFlags(-1);
        cifsConfig.setTcpipSMB(true);
    }
}
