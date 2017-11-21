/**
 * Copyright (C) 2008 Happy Fish / YuQing
 * <p>
 * FastDFS Java Client may be copied only under the terms of the GNU Lesser
 * General Public License (LGPL).
 * Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
 **/

package com.github.landyking.learnFastdfs;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.util.Arrays;

/**
 * client test
 *
 * @author Happy Fish / YuQing
 * @version Version 1.18
 */
public class Test {
  private Test() {
  }

  /**
   * entry point
   *
   * @param args comand arguments
   *             <ul><li>args[0]: config filename</li></ul>
   *             <ul><li>args[1]: local filename to upload</li></ul>
   */
  public static void main(String args[]) {
    System.out.println("java.version=" + System.getProperty("java.version"));

    String conf_filename = Test.class.getResource("/fdfs_client.conf").getFile();
    String local_filename = conf_filename;

    try {
      ClientGlobal.init(conf_filename);
      System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
      System.out.println("charset=" + ClientGlobal.g_charset);

      TrackerClient tracker = new TrackerClient();
      TrackerServer trackerServer = tracker.getConnection();
      StorageServer storageServer = null;
      StorageClient1 client = new StorageClient1(trackerServer, storageServer);

      NameValuePair[] metaList = new NameValuePair[1];
      metaList[0] = new NameValuePair("fileName", local_filename);
      String fileId = client.upload_file1(local_filename, "conf", metaList);
      System.out.println("upload success. file id is: " + fileId);
      FileInfo file_info1 = client.get_file_info1(fileId);
      System.out.println("file info : "+file_info1.toString());
      NameValuePair[] metadata1 = client.get_metadata1(fileId);
      System.out.println("file meta : ");
      for (NameValuePair one : metadata1) {
        System.out.println("\t" + one.getName() + ":" + one.getValue());
      }
      int i = 0;
      while (i++ < 10) {
        byte[] result = client.download_file1(fileId);
        System.out.println(i + ", download result is: " + result.length);
      }

      trackerServer.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}