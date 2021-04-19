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
@Table(name="node")
public class Node {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("案例公司名称")
    private String one;

    @ApiModelProperty("案例公司名称")
    private String two;
    
    @ApiModelProperty("案例公司名称")
    private String three;
    
    @ApiModelProperty("案例公司名称")
    private String four;
    
    @ApiModelProperty("案例公司名称")
    private String five;
    
    @ApiModelProperty("案例公司名称")
    private String six;
    
    @ApiModelProperty("案例公司名称")
    private String seven;
    
    @ApiModelProperty("案例公司名称")
    private String eight;
    
    @ApiModelProperty("案例公司名称")
    private String nine;

}