package com.github.landyking.learnFastdfs;

/**
 * Created by landy on 2017/11/21.
 */
public class FdfsFileInfo {
    private String fileId;
    private Long fileSize;
    private String fileName;
    public FdfsFileInfo(String fileId, Long fileSize, String fileName) {
        this.fileId = fileId;
        this.fileSize = fileSize;
        this.fileName = fileName;
    }

    public FdfsFileInfo() {
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
