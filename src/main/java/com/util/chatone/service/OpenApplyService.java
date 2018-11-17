package com.util.chatone.service;

import com.alibaba.fastjson.JSONObject;
import com.util.chatone.bean.OpenApplyReq;
import com.util.chatone.bean.OpenResultReq;
import com.util.chatone.bean.OpenResultRes;
import com.util.chatone.common.HeadReq;
import com.util.chatone.common.ObjectReq;
import com.util.chatone.utils.HttpRequest;
import com.util.chatone.utils.JsonUtils;
import com.util.chatone.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2018/11/8
 * Time: 19:03
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Slf4j
@Service
public class OpenApplyService {

    public void openApply(OpenApplyReq openApplyReq1){

        ObjectReq<OpenApplyReq> objReq = new ObjectReq<>();
        HeadReq headReq = genHeadReq();
        objReq.setHead(headReq);

//        OpenApplyReq openApplyReq = new OpenApplyReq();
//        openApplyReq.setClientRequestNo("12341234");
//        openApplyReq.setBillNo("1234");
//        openApplyReq.setBillCode("1231234");
//        openApplyReq.setBillName("23412");
//        openApplyReq.setOpenName("核心6");
//        openApplyReq.setOpenUsccCode("525100006922745709");
//        openApplyReq.setReceiveName("韦迪供应商6");
//        openApplyReq.setReceiveUsccCode("52510000588372146X");
//        openApplyReq.setJchtNo("2018110811222222222222");
//        openApplyReq.setJchtDate("20181108");
//        openApplyReq.setJchtName("1234");
//        openApplyReq.setJchtAmount("10000");
//        openApplyReq.setApplyAmount("100");
//        openApplyReq.setPayDate("20181108");
//        openApplyReq.setPaymentDesc("122");
//        openApplyReq.setFileId("5beaa13c5a2b5a0155e8bbcc");

        objReq.setBody(openApplyReq1);
        try {
            String sign = SignUtil.sign(objReq);
            headReq.setSign(sign);
            //2、发送请求
            String reStr = this.send("/web/ec/openEc.htm", JSONObject.toJSONString(objReq));
            log.info("请求返回：{}",reStr);

        }catch (Exception e){

        }
    }

    public OpenResultRes openResultQuery(String openNo){
        ObjectReq<OpenResultReq> objReq = new ObjectReq<>();
        HeadReq headReq = genHeadReq();
        objReq.setHead(headReq);
        OpenResultRes openResultRes = new OpenResultRes();
        OpenResultReq openResultReq = new OpenResultReq();
        openResultReq.setOpenNo(openNo);

        objReq.setBody(openResultReq);
        String reStr ="";
        try {
            String sign = SignUtil.sign(objReq);
            headReq.setSign(sign);
            //2、发送请求
            reStr = this.send("/web/ec/openResult.htm", JSONObject.toJSONString(objReq));
            log.info("请求返回：{}",reStr);

            openResultRes = JsonUtils.jsonToObject(reStr,OpenResultRes.class);

        }catch (Exception e){
            e.getMessage();
            log.info("查询出错"+reStr);
        }
        return openResultRes;
    }

    public HeadReq genHeadReq(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String requestTime =df.format(new Date());
        HeadReq req = new HeadReq();
//        req.setClientId(signConfig.getCertKey());
        req.setClientId("erp_scm");
        req.setVersion("1.0");
        req.setRequestTime(requestTime);
        return req;
    }

    private String send(String mapUrl,String paramStr){
//        String url = "http://localhost:9001/scm-web";//for test
        String url = "http://222.84.157.37:13001/scm-web";
        String reStr = HttpRequest.fmcPost(url, mapUrl, paramStr);
        return reStr;
    }
}
