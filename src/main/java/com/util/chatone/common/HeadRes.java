package com.util.chatone.common;


import lombok.Data;

/**
 * 公用请求头
 */
@Data
public class HeadRes {

    //true：成功 false：失败，详见错误码和错误描述；如果为false，则无需再处理body节点内容（一般返回为空）
    private boolean result;//

    private String errorCode;//  错误码		String(14)	head	Y

    private String errorMsg;//错误描述	String(512)	head	Y	错误信息

    private String responseTime;//响应时间		String(14)	head	N

    private String sign;//签名串		String(512)	head	N	响应报文签名串

    public boolean getResult() {//该方法保留！！加签方法getAttribute()中是通过get方法获取result值。boolean类型使用@Data直接生成的话是isResult
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
    
    
}
