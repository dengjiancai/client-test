package com.util.chatone.bean;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2018/11/7
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Data
public class OpenResultReq {
    //
    @NotBlank(message = "出票申请编号不能为空")
    @Length(max = 50, message = "出票申请编号不能超过50位")
    private String  openNo;

}
