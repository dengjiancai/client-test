package com.util.chatone.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2018/11/7
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Data
public class OpenApplyReq {
    //
    @NotEmpty(message = "申请流水号不能为空")
    @Length(max = 20, message = "申请流水号长度不能超过20位")
    @ApiModelProperty(required = true, example = "10001", value = "申请流水号")
    private String  clientRequestNo;

    //
    @NotEmpty(message = "流程编号不能为空")
    @Length(max = 20, message = "流程编号长度不能超过20位")
    @ApiModelProperty( example = "12341234", value = "流程编号")
    private String billNo ;

    //流程名称
    @NotEmpty(message = "流程名称不能为空")
    @ApiModelProperty( example = "流程名称", value = "流程名称")
    private String billName;

    //报账单编号
    @NotEmpty(message = "报账单编号不能为空")
    @ApiModelProperty( example = "100011111", value = "报账单编号")
    private String billCode;

    //E信开具方统一社会信用代码
    @NotEmpty(message = "E信开具方统一社会信用代码不能为空")
    @ApiModelProperty( example = "525100006922745709", value = "E信开具方")
    private String openUsccCode;

    //E信开具方企业名称
    @NotEmpty(message = "E信开具方企业名称不能为空")
    @ApiModelProperty( example = "核心6", value = "E信开具方企业名称")
    private String openName;

    //接收方统一社会信用代码证
    @NotEmpty(message = "接收方统一社会信用代码证不能为空")
    @ApiModelProperty( example = "52510000588372146X", value = "接收方")
    private String receiveUsccCode;

    //接收方企业名称
    @NotEmpty(message = "接收方企业名称不能为空")
    @ApiModelProperty( example = "韦迪供应商6", value = "接收方企业名称")
    private String receiveName;

    //合同签署日
    @NotEmpty(message = "合同签署日不能为空")
    @ApiModelProperty( example = "20181108", value = "合同签署日")
    private String jchtDate;

    //合同名称
    @NotEmpty(message = "合同名称不能为空")
    @ApiModelProperty( example = "合同名称", value = "合同名称")
    private String jchtName;

    //合同名称
    @NotEmpty(message = "合同编号不能为空")
    @ApiModelProperty( example = "ERP合同编号", value = "ERP合同编号")
    private String jchtNo;

    @NotEmpty(message = "合同金额不能为空")
    @Digits(fraction = 0, integer = 20,message="合同金额[jchtAmount]必须为20位以内的整数。")
    @Min(value = 1, message = "合同金额单位为分，必须是大于0的整数")
    @ApiModelProperty( example = "10001000", value = "合同金额")
    private String jchtAmount;

    @NotEmpty(message = "申请金额不能为空")
    @Digits(fraction = 0, integer = 20,message="申请金额[applyAmount]必须为20位以内的整数。")
    @Min(value = 1, message = "申请金额单位为分，必须是大于0的整数")
    @ApiModelProperty( example = "50000", value = "申请金额")
    private String applyAmount;

    @NotEmpty(message = "付款说明不能为空")
    @ApiModelProperty( example = "付款说明付款说明付款说明付款说明付款说明付款说明", value = "付款说明")
    private String paymentDesc;

    @NotEmpty(message = "承诺付款日不能为空")
    @ApiModelProperty( example = "20181118", value = "承诺付款日")
    private String  payDate;

    @NotEmpty(message = "交易背景资料不能为空")
    @ApiModelProperty( example = "5beb89c65a2b5a0155e8bbd2", value = "交易背景资料")
    private String fileId;

}
