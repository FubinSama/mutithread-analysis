package com.wfb.mutithreadanalysis.controller;

import com.wfb.mutithreadanalysis.model.ClassFile;
import com.wfb.mutithreadanalysis.model.ResponseBean;
import com.wfb.mutithreadanalysis.service.FileService;
import com.wfb.mutithreadanalysis.util.Paramters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@RestController
public class FileUploadController {

    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public ResponseBean upload(@RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile == null) return ResponseBean.error("服务器无法接收该文件，请重试！");
        String multipartFileName = multipartFile.getOriginalFilename();
        String[] strs = multipartFileName.split("\\.");
        if (strs.length != 2 || !strs[1].equals("java")) {
            return ResponseBean.error("请上传一个后缀为.java的文件");
        }
        String packagePath = "";
        try (Scanner sc = new Scanner(multipartFile.getInputStream())){
            String firstLine = sc.nextLine().trim().substring(8).trim();
            if (firstLine.contains("package"))
                packagePath = firstLine.substring(0, firstLine.length() - 1).replaceAll(".", "/");
        } catch (Exception e) {
            ResponseBean.error("无法读取该文件！");
        }
        if (packagePath.length() != 0) packagePath += "/";
        String fileName = "test/" + packagePath + multipartFileName;
        File file = new File(Paramters.CALFUZZER_PATH + fileName);
        file.mkdir();
        System.out.println(file.getAbsolutePath());
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            return ResponseBean.error("无法存放该文件，请重试！");
        }
        /**
         * javac test/test/wfb/Test.java -d classes
         * ant -f run.xml -DclassName=test.wfb.Test
         */
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = null;
            String javac = "javac " + Paramters.CALFUZZER_PATH + fileName + " -d " + Paramters.CALFUZZER_PATH + "classes/";;
            p = runtime.exec(javac);
            int rt = p.waitFor();
            if (rt != 0) return ResponseBean.error("该文件无法被javac编译，请查看后重新上传！");
            String className = packagePath.replaceAll("/", ".") + strs[0];
            String ant = "ant -f " + Paramters.CALFUZZER_PATH + "run.xml -DclassName=" + className;
            p = runtime.exec(ant);
            boolean isExit = p.waitFor(100, TimeUnit.SECONDS);
            if (!isExit) return ResponseBean.error("程序解析时间超过100s，请保证程序中不存在死循环！");
            if (p.exitValue() != 0) return ResponseBean.error("该程序无法被解析！");

            fileService.add(new ClassFile(0, className, className.replaceAll("//.", "_")));

            return ResponseBean.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error("无法解析该程序，请重试！！！");
        }
    }
}
