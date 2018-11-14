package com.util.chatone.controller;

import com.util.chatone.bean.OpenApplyReq;
import com.util.chatone.bean.OpenResultRes;
import com.util.chatone.common.ObjectRes;
import com.util.chatone.service.OpenApplyService;
import com.util.chatone.utils.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2018/11/8
 * Time: 17:55
 * To change this template use File | Settings | File Templates.
 * Description:
 */

@RestController
@RequestMapping("/api/trans/test")
@Api(tags = "000-CmsbFmSysController", description = "测试管理")
@Slf4j
public class ForTestController {

    @Autowired
    OpenApplyService openApplyService;

    @PostMapping("/uploadFileTest")
    @ApiOperation(value = "上传文件测试")


    @ApiImplicitParams({
            @ApiImplicitParam(value = "请求URL", name = "url", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "文件路径", name = "filePath", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "requestNo", name = "requestNo", required = true, dataType = "String", paramType = "query")
    })
    public void uploadFileTest(@RequestParam String filePath,@RequestParam String url,@RequestParam String requestNo) {

        try {

//            HttpUtil.uploadByerp(filePath,url,requestNo);
            HttpUtil.upload(filePath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/openApply")
    @ApiOperation(value = "商票开立")
    public ObjectRes<OpenResultRes> openApplyTest(@RequestBody OpenApplyReq openApplyReq) {
        OpenResultRes bodyRes = new OpenResultRes();
        openApplyService.openApply(openApplyReq);

        ObjectRes<OpenResultRes> objRes = new ObjectRes<OpenResultRes>();
        objRes.getHead().setResult(true);
        objRes.getHead().setErrorCode("0000");
        objRes.getHead().setErrorMsg("处理成功");
        objRes.setBody(bodyRes);
        return objRes;
    }

    @PostMapping("/openResult.htm")
    @ApiOperation(value = "商票开立查询")
    @ApiImplicitParam(value = "出票编号", name = "openNo", required = true, dataType = "String", paramType = "query")
    public void openApplyResult(@RequestParam String openNo) {

        openApplyService.openResultQuery(openNo);
    }
}
