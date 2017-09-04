package com.baba.controller;

import com.baba.dictionary.Constants;
import com.baba.utils.FastDFSTool;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 上传文件控制器
 * @author: Jamayette
 * Created on  2017/8/15
 */
@Controller
public class UploadController {

    @RequestMapping("/uploadFile.do")
    @ResponseBody
    public HashMap<String,String> uploadFile(@RequestParam("mpf")MultipartFile mpf) throws Exception {

        // 将文件上传到分布式文件系统，并返回文件的存储路径及名称
        String uploadFile = FastDFSTool.uploadFile(mpf.getBytes(), mpf.getOriginalFilename());
        // 返回json格式的字符串（图片的绝对网络存放地址）
        HashMap<String, String> map = new HashMap<>();
        map.put("path", Constants.FDFS_SERVER + uploadFile);

        return map;
    }

    @RequestMapping("/uploadFiles.do")
    @ResponseBody
    public List<String> uploadFiles(@RequestParam("mpfs")MultipartFile[] mpfs) throws Exception {

        //上传文件返回路径的集合
        List<String> pathList = new ArrayList<>();
        for (MultipartFile mpf : mpfs) {
            String uploadFile = FastDFSTool.uploadFile(mpf.getBytes(), mpf.getOriginalFilename());

            // 将文件上传到分布式文件系统，并返回文件的存储路径及名称
            pathList.add(Constants.FDFS_SERVER+uploadFile);
        }

        return pathList;

    }

    @RequestMapping("/uploadFck.do")
    @ResponseBody
    public Map<String,Object> uploadFCK(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 将request强转为spring提供的MultipartRequest
        MultipartRequest mr = (MultipartRequest) request;

        // 获得MultipartRequest里面的所有文件
        Set<Map.Entry<String, MultipartFile>> entrySet = mr.getFileMap().entrySet();

        for (Map.Entry<String, MultipartFile> entry : entrySet) {
            MultipartFile mpf = entry.getValue();

            // 将文件上传到分布式文件系统，并返回文件的存储路径及名称
            String uploadFile = FastDFSTool.uploadFile(mpf.getBytes(),
                    mpf.getOriginalFilename());

            // 返回json格式的字符串（图片的绝对网络存放地址）
            Map<String, Object> map = new HashMap<String, Object>();

            // error和url名字都是固定死的
            map.put("error", 0);
            map.put("url", Constants.FDFS_SERVER + uploadFile);
            return map;
        }
        return null;
    }

}
