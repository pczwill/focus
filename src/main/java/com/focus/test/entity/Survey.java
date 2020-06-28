package com.focus.test.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * cfg_survey
 * @author 
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="cfg_survey")
public class Survey {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("问卷名称")
    private String title;

    @ApiModelProperty("简要描述")
    private String detail;

    @ApiModelProperty("发布单位")
    private String unit;

    @ApiModelProperty("删除标志")
    private Integer delStatus;

    @ApiModelProperty("更新人")
    private Integer updatedBy;

    @ApiModelProperty("updated_at")
    private Date updatedAt;

    @ApiModelProperty("创建人")
    private Integer createdBy;

    @ApiModelProperty("created_at")
    private Date createdAt;

}