package com.util.chatone.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;


public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);


    /**
     * 发送post请求
     * @param postUrl
     * @param reqJson
     * @param headerMap
     * @param charset
     * @return
     */
    public static String postByJson(String postUrl, String reqJson, Map<String, String> headerMap, String charset) {

        String responseResult = "";
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建httppost
        HttpPost httppost = new HttpPost(postUrl);
        Charset charsetClass;
        try {
            if (null == charset || charset.trim().length() < 1) {
                charsetClass = Charset.forName("UTF-8");
            } else {
                charsetClass = Charset.forName(charset);
            }

            StringEntity se = new StringEntity(reqJson, charsetClass);
            httppost.setEntity(se);
            //设置http header
            if (headerMap != null && !headerMap.isEmpty()) {
                Iterator<String> it = headerMap.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = headerMap.get(key);
                    httppost.setHeader(key, value);
                }
            }

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();//设置请求和传输超时时间
            httppost.setConfig(requestConfig);

            log.info("请求地址：" + postUrl);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                log.info("响应吗：{}", response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new Exception("请求没有返回200响应码");
                }
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    responseResult = EntityUtils.toString(entity, "UTF-8");
                }

            } catch (Exception e1) {
                e1.printStackTrace();
                log.error("http请求错误", e1);
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("http请求错误", e);
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (Exception e2) {
                log.error("http请求错误", e2);
            }
        }

        return responseResult;

    }

    public static void upload(String localFile){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();

            // 把一个普通参数和文件上传给下面这个地址 是一个servlet
            HttpPost httpPost = new HttpPost("http://localhost:9001/scm-web/web/ec/uploadFile.htm");

            // 把文件转换成流对象FileBody
            FileBody bin = new FileBody(new File(localFile));
            bin.getFile();
            StringBody userName = new StringBody("123456", ContentType.create(
                    "text/plain", Consts.UTF_8));
            StringBody password = new StringBody("123456", ContentType.create(
                    "text/plain", Consts.UTF_8));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    // 相当于<input type="file" name="file"/>
                    .addPart("fileData", bin)

                    // 相当于<input type="text" name="userName" value=userName>
                    .addPart("fileRequestNo", userName)
                    .addPart("pass", password)
                    .build();

            httpPost.setEntity(reqEntity);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            // 发起请求 并返回请求的响应
            response = httpClient.execute(httpPost);

            System.out.println("The response value of token:" + response.getFirstHeader("token"));

            // 获取响应对象
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                // 打印响应长度
                System.out.println("Response content length: " + resEntity.getContentLength());
                // 打印响应内容
                System.out.println(EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
            }

            // 销毁
            EntityUtils.consume(resEntity);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void uploadByerp(String localFile,String Url, String requestNo){
        File file = new File(localFile);
        PostMethod filePost = new PostMethod("http://localhost:9001/scm-web/web/ec/uploadFile.htm");
        HttpClient client = new HttpClient();

        try {
            // 通过以下方法可以模拟页面参数提交
            filePost.setParameter("fileRequestNo", requestNo);
//            filePost.setParameter("passwd", passwd);

            Part[] parts = { new FilePart(file.getName(), file) };
            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));

            client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

            int status = client.executeMethod(filePost);
            if (status == HttpStatus.SC_OK) {
                System.out.println("上传成功");
            } else {
                System.out.println("上传失败");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            filePost.releaseConnection();
        }
    }


}
