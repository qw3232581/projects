package com.baba.utils;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * FastDFS工具类
 * @author Jamayette
 * Created on  2017/8/15
 */
public class FastDFSTool {

    /**
     * 上传文件
    * @author Jamayette 2017/8/15
    * @param bs 文件字节数组
    * @param filename 文件名
    * @return 上传成功后，存放在fastdfs中的文件位置及文件名
    */
    public static String uploadFile(byte[] bs, String filename)
            throws FileNotFoundException, IOException, Exception {
        // 获得classpath下文件的绝对路径
        ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
        // 初始化客户端
        ClientGlobal.init(resource.getClassLoader()
                .getResource("fdfs_client.conf").getPath());

        // 创建Tracker客户端
        TrackerClient trackerClient = new TrackerClient();
        // 通过Tracker客户端取得连接获得Tracker服务器端
        TrackerServer connection = trackerClient.getConnection();
        // Storage客户端
        StorageClient1 storageClient1 = new StorageClient1(connection, null);
        // 获得文件名的扩展名
        String extension = FilenameUtils.getExtension(filename);

        // 通过Storage客户端开始上传文件，并返回存放在fastdfs中的文件位置及文件名
        // 例如： group1/M00/00/00/wKg4ZViGbUOAWYBOAAFcP6dp0kY652.jpg
        return storageClient1.upload_file1(bs, extension, null);

    }

}
