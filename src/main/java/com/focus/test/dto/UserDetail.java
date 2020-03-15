package com.focus.test.dto;

import java.io.Serializable;
import java.util.List;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户详情")
public class UserDetail implements Serializable  {
	private static final long serialVersionUID = 3621271437883980835L;

	@ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("公司id")
    private Integer companyId;
    
    @ApiModelProperty("关联公司Id")
    private Integer groupId;
    
    @ApiModelProperty("公司名")
    private String companyName;
    
    @ApiModelProperty("部门id")
    private Integer departmentId;
    
    @ApiModelProperty("职位id")
    private Integer positionId;
    
    @ApiModelProperty("部门名")
    private String departmentName;
    
    @ApiModelProperty("职位名")
    private String positionName;
    
    @ApiModelProperty("发邮件类型")
    private String emailType;
    
    @ApiModelProperty("邮箱")
    private String email;
    
    @ApiModelProperty("使用监控时间")
    private String dateStr;
    
	@ApiModelProperty("小程序openId")
    private String miniOpenId;
	
	@ApiModelProperty("网页openId")
    private String webOpenId;

	@ApiModelProperty("unionId")
    private String unionId;
    
    @ApiModelProperty("微信昵称")
    private String nickName;
    
    @ApiModelProperty("用户状态(1正常2禁用)")
    private Integer userStatus;
    
    @ApiModelProperty("公告状态(0无1有)")
    private Integer noticeStatus;
    
}
