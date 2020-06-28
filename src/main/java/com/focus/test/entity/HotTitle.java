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
 * hot_title
 * @author 大狼狗 2020-05-14
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="hot_title")
public class HotTitle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("所属集团")
    private Integer parentId;

    @ApiModelProperty("案例公司名称")
    private String companyName;

    @ApiModelProperty("created_at")
    private Date createdAt;

    @ApiModelProperty("updated_at")
    private Date updatedAt;

    @ApiModelProperty("软删除标志  1-是 0-否")
    private Integer delStatus;

    @ApiModelProperty("创建人")
    private Integer createdBy;

    @ApiModelProperty("修改人")
    private Integer updatedBy;

    @ApiModelProperty("企业统一信用代码")
    private String code;

    @ApiModelProperty("股票编码")
    private String stockCode;

    @ApiModelProperty("股票中文名")
    private String displayName;

    @ApiModelProperty("行业编码")
    private String title;
    
    @ApiModelProperty("行业编码")
    private String time;

    @ApiModelProperty("行业编码")
    private String event;

    @ApiModelProperty("行业名称")
    private String companyCount;


}