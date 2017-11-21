package com.github.landyking.learnFastdfs;

import com.google.common.base.Splitter;
import com.google.common.io.Files;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by landy on 2017/11/21.
 */
@Service
public class FastdfsClientService implements InitializingBean {
    @Resource
    private Environment environment;
    private StorageClient1 client;
    private TrackerServer trackerServer;

    public FdfsFileInfo uploadFile(MultipartFile file) throws IOException, MyException {
        byte[] data = file.getBytes();
        String originalFilename = file.getOriginalFilename();
//        String fileId = client.upload_file1(data, Files.getFileExtension(originalFilename), null);
        String fileExtension = Files.getFileExtension(originalFilename);
        NameValuePair[] meta_list = new NameValuePair[]{new NameValuePair("fileName", originalFilename)};
        String fileId = uploadFile0(data, fileExtension, meta_list);
        FdfsFileInfo rst = new FdfsFileInfo();
        rst.setFileId(fileId);
        rst.setFileName(originalFilename);
        rst.setFileSize((long) data.length);
        return rst;
    }

    private String uploadFile0(byte[] data, String fileExtension, NameValuePair[] meta_list) throws IOException, MyException {
        try {
            return client.upload_file1(data, fileExtension, meta_list);
        } catch (Exception e) {
            resetClient();
            return client.upload_file1(data, fileExtension, meta_list);
        }
    }

    private void resetClient() throws IOException {
        if (trackerServer != null) {
            try {
                trackerServer.close();
            } catch (Exception e) {

            }
            trackerServer = null;
        }
        ClientGlobal.setG_tracker_http_port(environment.getProperty("fastdfs.tracker_http_port", Integer.class, 8080));
        String servers = environment.getRequiredProperty("fastdfs.tracker_server");
        Assert.hasText(servers, "fastdfs.tracker_server is empty");
        List<InetSocketAddress> list = new ArrayList<InetSocketAddress>();
        Iterable<String> split = Splitter.on(",").split(servers);
        for (String one : split) {
            List<String> tmp = Splitter.on(":").omitEmptyStrings().splitToList(one);
            Assert.isTrue(tmp.size() == 2, "fastdfs.tracker_server format error");
            list.add(new InetSocketAddress(tmp.get(0), Integer.parseInt(tmp.get(1))));
        }
        ClientGlobal.setG_tracker_group(new TrackerGroup(list.toArray(new InetSocketAddress[list.size()])));
        System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
        System.out.println("charset=" + ClientGlobal.g_charset);

        TrackerClient tracker = new TrackerClient();
        trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        client = new StorageClient1(trackerServer, storageServer);
    }

    public FdfsFileInfo getFileInfo(String fileId) throws IOException, MyException {
        FileInfo file_info1 = client.get_file_info1(fileId);
        NameValuePair[] metadata1 = client.get_metadata1(fileId);
        FdfsFileInfo rst = new FdfsFileInfo();
        rst.setFileName(getFileName(metadata1));
        rst.setFileSize(file_info1.getFileSize());
        rst.setFileId(fileId);
        return rst;
    }

    private String getFileName(NameValuePair[] metadata1) {
        for (NameValuePair one : metadata1) {
            if (one.getName().equals("fileName")) {
                return one.getValue();
            }
        }
        return "";
    }

    public byte[] downloadFile(String fileId) throws IOException, MyException {
        return client.download_file1(fileId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        resetClient();
    }
}
