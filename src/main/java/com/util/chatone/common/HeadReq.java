package com.util.chatone.common;

/**
 * 公用请求头
 */


public class HeadReq {
    
    private String clientId;//客户编号		HeadReq(12)	head	N	由东信系统统一分配


    private String requestTime;//请求时间		HeadReq(14)	head	N	格式：yyyyMMddHHmmss


    private String version = "1.0";//版本号		HeadReq(8)	head	N	固定值：1.0


    private String sign;//签名串	HeadReq(512)	head	N	请求报文签名串
    
    public String getClientId() {
        return clientId;
    }


    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public String getRequestTime() {
        return requestTime;
    }


    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }


    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }


    public String getSign() {
        return sign;
    }


    public void setSign(String sign) {
        this.sign = sign;
    }
    
    

}
