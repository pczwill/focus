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
@Table(name="self")
public class Self {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("")
    private String one;

    @ApiModelProperty("")
    private String two;
    
    @ApiModelProperty("")
    private String three;
    
    @ApiModelProperty("")
    private String four;
    
    @ApiModelProperty("")
    private String five;
    
    @ApiModelProperty("")
    private String six;
    
    @ApiModelProperty("")
    private String seven;
    
    @ApiModelProperty("")
    private String eight;
    
    @ApiModelProperty("")
    private String nine;

    @ApiModelProperty("")
    private String ten;
}