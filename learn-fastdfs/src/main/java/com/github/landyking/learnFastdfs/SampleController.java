package com.github.landyking.learnFastdfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.apache.log4j.spi.LoggerFactory;
import org.csource.common.MyException;
import org.csource.fastdfs.FileInfo;
import org.slf4j.Logger;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.github.landyking.learnFastdfs")
public class SampleController {
    ObjectMapper JSON = new ObjectMapper();
    @Resource
    private FastdfsClientService clientService;
    private Logger logger;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }

    @RequestMapping("/uploadFile")
    @ResponseBody
    public Object uploadFile(MultipartFile[] files) throws IOException, MyException {
        Assert.notEmpty(files, "no file upload");
        List<FdfsFileInfo> rst = new ArrayList<FdfsFileInfo>();
        for (MultipartFile one : files) {
            FdfsFileInfo tmp = clientService.uploadFile(one);
            logger = org.slf4j.LoggerFactory.getLogger(getClass());
            logger.info("one file upload, name: {}, size: {}, fileId: {}",tmp.getFileName(),tmp.getFileSize(),tmp.getFileId());
            rst.add(tmp);
        }
        return rst;
    }
    @RequestMapping("/fileInfo")
    @ResponseBody
    public Object fileInfo(String fileId) throws IOException, MyException {
        Assert.hasText(fileId, "fileId is empty");
        FdfsFileInfo fileInfo = clientService.getFileInfo(fileId);
        logger.info("one file query, name: {}, size: {}, fileId: {}",fileInfo.getFileName(),fileInfo.getFileSize(),fileInfo.getFileId());
        return fileInfo;
    }
    @RequestMapping("/download")
    public void download(String fileId, HttpServletResponse response) throws IOException, MyException {
        Assert.hasText(fileId, "fileId is empty");
        byte[] bytes = clientService.downloadFile(fileId);
        FdfsFileInfo tmp = clientService.getFileInfo(fileId);
        logger.info("one file download, name: {}, size: {}, fileId: {}",tmp.getFileName(),tmp.getFileSize(),tmp.getFileId());
        String fileName = tmp.getFileName();
        fileName = URLEncoder.encode(fileName, Charsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
        response.getOutputStream().write(bytes);
        response.getOutputStream().close();
    }
}