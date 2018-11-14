package com.util.chatone.common;

import java.io.Serializable;

/**
 * 请求数据定义
 * @version 1.0
 * @author Weisq
 * @data 2017/11/28 18:07
 */
public class ObjectReq<T> implements Serializable{

    private static final long serialVersionUID = 5080109401212144780L;

	private HeadReq head;//请求头节点

	private T body;//请求业务节点

    public HeadReq getHead() {
        return head;
    }

    public void setHead(HeadReq head) {
        this.head = head;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
	
}
