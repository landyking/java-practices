package com.github.landyking.learnFastdfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.csource.common.MyException;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SampleController {
    ObjectMapper JSON = new ObjectMapper();
    @Resource
    private FastdfsClientService clientService;
    private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }


    @RequestMapping("/uploadFile")
    @ResponseBody
    public Object uploadFile(MultipartFile[] files) throws IOException, MyException {
        Assert.notEmpty(files, "no file upload");
        List<FdfsFileInfo> rst = new ArrayList<FdfsFileInfo>();
        for (MultipartFile one : files) {
            FdfsFileInfo tmp = clientService.uploadFile(one);

            logger.info("one file upload, name: {}, size: {}, fileId: {}", tmp.getFileName(), tmp.getFileSize(), tmp.getFileId());
            rst.add(tmp);
        }
        return rst;
    }

    @RequestMapping("/fileInfo")
    @ResponseBody
    public Object fileInfo(String fileId, HttpServletResponse response) throws IOException, MyException {
        Assert.hasText(fileId, "fileId is empty");
        FdfsFileInfo fileInfo = clientService.getFileInfo(fileId);
        if (fileInfo != null) {
            logger.info("one file query, name: {}, size: {}, fileId: {}", fileInfo.getFileName(), fileInfo.getFileSize(), fileInfo.getFileId());
            return fileInfo;
        } else {
            return JSON.createObjectNode().put("success", false).put("msg", "file: " + fileId + " not exist!");
        }
    }

    @RequestMapping("/download")
    @ResponseBody
    public Object download(String fileId, HttpServletResponse response) throws IOException, MyException {
        Assert.hasText(fileId, "fileId is empty");
        FdfsFileInfo tmp = clientService.getFileInfo(fileId);
        if (tmp == null) {
            return JSON.createObjectNode().put("success", false).put("msg", "file: " + fileId + " not exist!");
        }
        byte[] bytes = clientService.downloadFile(fileId);
        logger.info("one file download, name: {}, size: {}, fileId: {}", tmp.getFileName(), tmp.getFileSize(), tmp.getFileId());
        String fileName = tmp.getFileName();
        fileName = URLEncoder.encode(fileName, Charsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
        response.getOutputStream().write(bytes);
        response.getOutputStream().close();
        return null;
    }
}