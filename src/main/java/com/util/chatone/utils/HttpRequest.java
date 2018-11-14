package com.util.chatone.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

public class HttpRequest {
	
	private static Logger logger = LoggerFactory.getLogger(HttpRequest.class);
	private static final Charset charset = Charset.forName("GBK");

    private static PoolingHttpClientConnectionManager cm = null;

    static {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
        	logger.error("创建SSL连接失败");
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        cm =new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
    }

    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    public static String get(String url) {
        CloseableHttpClient httpClient = HttpRequest.getHttpClient();
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet get = new HttpGet(url);
            httpResponse = httpClient.execute(get);
            HttpEntity entity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return EntityUtils.toString(entity,charset);
            }
        } catch (IOException e) {
        	logger.error("httpclient请求失败", e);
        } finally {
            if (httpResponse != null) {
                try {
                    EntityUtils.consume(httpResponse.getEntity());
                    httpResponse.close();
                } catch (IOException e) {
                	logger.error("关闭response失败", e);
                }
            }
        }
        return null;
    }
    
    public static String post(String url,String str) {
        CloseableHttpClient httpClient = HttpRequest.getHttpClient();
        CloseableHttpResponse httpResponse = null;
        try {
            HttpPost post = new HttpPost(url);
            StringEntity se = new StringEntity(str, charset);
            post.setEntity(se);
            httpResponse = httpClient.execute(post);
            HttpEntity entity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return EntityUtils.toString(entity,charset);
            }
        } catch (IOException e) {
        	logger.error("httpclient请求失败", e);
        } finally {
            if (httpResponse != null) {
                try {
                    EntityUtils.consume(httpResponse.getEntity());
                    httpResponse.close();
                } catch (IOException e) {
                	logger.error("关闭response失败", e);
                }
            }
        }
        return null;
    }
    
    /**请求fmc接口
     * 
     * @param url 服务地址
     * @param mapUrl 接口url
     * @param paramStr 参数
     * @return
     */
    public static String fmcPost(String url,String mapUrl,String paramStr){
        CloseableHttpClient httpClient = HttpRequest.getHttpClient();
        CloseableHttpResponse httpResponse = null;
        try {
            logger.info(url+mapUrl);
            HttpPost post = new HttpPost(url+mapUrl);
            StringEntity se = new StringEntity(paramStr, Charset.forName("UTF-8"));
            se.setContentType("application/json");
            post.setEntity(se);
            httpResponse = httpClient.execute(post);
            HttpEntity entity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return EntityUtils.toString(entity,Charset.forName("UTF-8"));
            }
        } catch (IOException e) {
            logger.error("httpclient请求失败", e);
        } finally {
            if (httpResponse != null) {
                try {
                    EntityUtils.consume(httpResponse.getEntity());
                    httpResponse.close();
                } catch (IOException e) {
                    logger.error("关闭response失败", e);
                }
            }
        }
        return null;
    }
    
//    public static void main(String[] args) {
//    	String str = "<?xml version=\"1.0\" encoding=\"GBK\"?><stream><action>DLSBALQR</action><userName>hegangzl</userName><accountNo>8111801013500468705</accountNo><subAccNo>3111810043674051642</subAccNo></stream>";
//		System.out.println(HttpRequest.post("http://220.194.242.39:6789",str));
//    	/*System.out.println(HttpRequest.get("http://www.baidu.com"));*/
//	}
}
