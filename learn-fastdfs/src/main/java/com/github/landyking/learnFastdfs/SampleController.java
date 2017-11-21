package com.github.landyking.learnFastdfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.csource.common.MyException;
import org.csource.fastdfs.FileInfo;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.github.landyking.learnFastdfs")
public class SampleController {
    ObjectMapper JSON = new ObjectMapper();
    @Resource
    private FastdfsClientService clientService;
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
            rst.add(tmp);
        }
        return rst;
    }
    @RequestMapping("/fileInfo")
    @ResponseBody
    public Object fileInfo(String fileId) throws IOException, MyException {
        Assert.hasText(fileId, "fileId is empty");
        FdfsFileInfo fileInfo = clientService.getFileInfo(fileId);
        return fileInfo;
    }
}