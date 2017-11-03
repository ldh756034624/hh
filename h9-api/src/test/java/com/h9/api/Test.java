package com.h9.api;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by itservice on 2017/11/2.
 */
public class Test {

    public String accessKey = "jSjj4DZYDPO2l00Iejd7MnUNUCapVd_IDu1m7LNq";
    public String secretKey = "3wKtwQGZfA7xFIsAt3I1LDaOR_kF6UVvRojLdi9k";
    String bucket = "h9-joyful-img";



    public static void main(String[] args) throws UnsupportedEncodingException {

//        upload(); // FoHkjc0XuS0niM_RKKJkWiGZLC5q
    }

    @org.junit.Test
    public  void downLoad() throws UnsupportedEncodingException {
        String fileName = "FoHkjc0XuS0niM_RKKJkWiGZLC5q";
        String domainOfBucket = "http://devtools.qiniu.com";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        System.out.println(finalUrl);
    }

    @org.junit.Test
    public void downLoad2() throws UnsupportedEncodingException {
        String fileName = "FoHkjc0XuS0niM_RKKJkWiGZLC5q";

        String domainOfBucket = "http://devtools.qiniu.com";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        System.out.println(finalUrl);
    }

    @org.junit.Test
    public    void upload(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "jSjj4DZYDPO2l00Iejd7MnUNUCapVd_IDu1m7LNq";
        String secretKey = "3wKtwQGZfA7xFIsAt3I1LDaOR_kF6UVvRojLdi9k";
        String bucket = "h9-joyful-img";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\itservice\\Pictures\\235113-1403260U22059.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    @org.junit.Test
    public void  getInfo(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        String key = "FoHkjc0XuS0niM_RKKJkWiGZLC5q";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            FileInfo fileInfo = bucketManager.stat(bucket, key);
            System.out.println(fileInfo.key);
            System.out.println(fileInfo.hash);
            System.out.println(fileInfo.fsize);
            System.out.println(fileInfo.mimeType);
            System.out.println(fileInfo.putTime);
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
    }
}