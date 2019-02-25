package me.lovexl.toutiao.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import me.lovexl.toutiao.interceptor.LoginRequiredInterceptor;
import me.lovexl.toutiao.util.toutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class QiniuService {
    private final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    @Autowired
    NewsService newsService;

    Configuration cfg = new Configuration(Zone.zone0());
    //...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);

    String accessKey = "GGUBXeMVqe2fQ8Sf-yxhBBhcfhedeebf2HPDspKK";
    String secretKey = "tnb7A7mj0td55O4ea_txqpe7Q4KQcTdqf3ILJhAt";
    String bucket = "lovexl";
    //如果是Windows情况下，格式是 D:\\qiniu\\test.png
   // String localFilePath = "/home/qiniu/test.png";
    //默认不指定key的情况下，以文件内容的hash值作为文件名
    Auth auth = Auth.create(accessKey, secretKey);
    String upToken = auth.uploadToken(bucket);

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int doPos = file.getOriginalFilename().lastIndexOf(".");
            if (doPos == 0) {
                return null;
            }
            String fileEXT = file.getOriginalFilename().substring(doPos + 1).toLowerCase();
            if (!toutiaoUtil.isFileAllowed(fileEXT)) {
                return null;
            }
            String key = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileEXT;
            Response response = uploadManager.put(file.getBytes(), key, upToken);
            //解析上传成功的结果
//            System.out.println(response.bodyString());
//            return null;
//            System.out.println(response.toString());
            if (response.isOK() && response.isJson()) {
                String keys = JSONObject.parseObject(response.bodyString()).get("key").toString();
                return toutiaoUtil.QINIU_DOMAIN_PREFIX + keys;
            }else
                logger.error("七牛异常"+response.bodyString());
                return null;
        }
        catch (QiniuException ex) {
            logger.error("七牛异常"+ ex.getMessage());
            return null;
        }
    }
}