package com.util.chatone.common;

import lombok.Data;
import org.slf4j.LoggerFactory;

/**
 * 请求数据定义
 * @version 1.0
 * @author Weisq
 * @data 2017/11/28 18:07
 */
@Data
public class ObjectRes<T>{

//    private static final long serialVersionUID = -8392565409632653758L;

	private HeadRes head;//返回头节点

	private T body;//返回业务节点

	public ObjectRes(){
	    
	}

	/**
	 * 唯一构造方法，保证head和body对象都创建
	 */
	public ObjectRes(Class<T> clazz){
		this.head= new HeadRes();
		try {
			this.body = clazz.newInstance();
		}catch (Exception e){
			LoggerFactory.getLogger(ObjectRes.class).error("初始化返回结果的Body对象[{]失败");
		}
	}


    public HeadRes getHead() {
        return head;
    }


    public void setHead(HeadRes head) {
        this.head = head;
    }


    public T getBody() {
        return body;
    }


    public void setBody(T body) {
        this.body = body;
    }
	
//	/**
//	 * 设置成功
//	 * @param body
//	 */
//	public void setResultSucc(T body){
//		this.head.setResult(true);
//		this.head.setResponseTime(CommUtils.generateTxnTime().substring(0,8));
//		this.body=body;
//	}
//
//	/**
//	 * 设置失败
//	 * @param errorCode
//	 * @param errorMsg
//	 */
//	public void setResultFail(String errorCode,String errorMsg){
//		this.head.setResult(false);
//		this.head.setErrorCode(errorCode);
//		this.head.setErrorMsg(errorMsg);
//		this.head.setResponseTime(CommUtils.generateTxnTime().substring(0,8));
//		//进行加签
//
//	}

}
