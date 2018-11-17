package com.util.chatone.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.util.chatone.bean.OpenApplyReq;
import com.util.chatone.bean.OpenApplyRes;
import com.util.chatone.bean.OpenResultReq;
import com.util.chatone.bean.OpenResultRes;
import com.util.chatone.bean.UploadFileRes;
import com.util.chatone.common.HeadReq;
import com.util.chatone.common.ObjectReq;
import com.util.chatone.common.ObjectRes;
import com.util.chatone.utils.HttpRequest;
import com.util.chatone.utils.HttpUtil;
import com.util.chatone.utils.JsonUtils;
import com.util.chatone.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
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

    public ObjectRes<UploadFileRes> uploadFile(String filePath, String url) throws Exception {
        try {

//            HttpUtil.uploadByerp(filePath,url,requestNo);
            String resJson = HttpUtil.upload(filePath,url);

            return JSON.parseObject(resJson,ObjectRes.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("文件上传失败");
        }
    }

    public  ObjectRes<OpenApplyRes> openApply(OpenApplyReq openApplyReq1) throws Exception {

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

//            JSONObject resJSON = JSONObject.parseObject(reStr);
            ObjectRes<OpenApplyRes> objectRes = JSON.parseObject(reStr,ObjectRes.class);
            return objectRes;
        }catch (Exception e){
            throw new Exception("接口调用失败");
        }
    }

    public static void main(String[] args) {
        String reStr = "{\"head\":{\"result\":true,\"errorCode\":\"0000\",\"errorMsg\":\"处理成功\",\"responseTime\":\"20181117191601\",\"sign\":\"60A4BFBEF28DB5F8AD590A004281BCF5C0D4A9ECFD856AA7C0C1354911D00AD50419A04FC91C34C9B488C114D097C11FD521E2BA2F1300E952F2A5EE4CF3002C5ED4A3650FFCF2ACEB424E1BE9763947D712AB6B44CBBF18EB29D4B66261600FF43F8E99ADF861C6958530A381952B23D0C0D65A4EDF63ED4E365FE5EC3BEDA09D415BDDEA286C83BA7BD17A4F779B0EDF9AD057EF7090C2BC714CEDB7B322F599C10197BC05A560F0E22108904E816914B101690E6CD633DEDA332C3E004939ECAE30D70E90AB697CB233A80AEF2864C683633DC86D801D180D409FC89CDFBD29997F8B69E3F524D5261FE3B57C09EF0745ADA4C56014150C50B1EFB22E995158B875427C66DDF48C8FCD75BBDAF055\"},\"body\":{\"openNo\":\"130672662890414080\"}}";
        JSONObject jo = JSONObject.parseObject(reStr);
        log.info(jo.toJSONString());
        ObjectRes<OpenResultRes> objectRes = JSON.parseObject(reStr, ObjectRes.class);
        log.info(objectRes.toString());
    }

    public ObjectRes<OpenResultRes> openResultQuery(String openNo) throws Exception {

        ObjectReq<OpenResultReq> objReq = new ObjectReq<>();
        HeadReq headReq = genHeadReq();
        objReq.setHead(headReq);
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
            return JSON.parseObject(reStr,ObjectRes.class);
        }catch (Exception e){
            e.getMessage();
            throw new Exception("状态查询失败");
        }
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
        String url = "http://localhost:9001/scm-web";//for test
//        String url = "http://222.84.157.37:13001/scm-web";
        String reStr = HttpRequest.fmcPost(url, mapUrl, paramStr);
        return reStr;
    }
}
