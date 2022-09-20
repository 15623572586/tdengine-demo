package com.yyh.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 喻云虎
 * @since 2022-08-09
 */
@Data
public class MonitorPointData implements Serializable {

    // table
    private String stName; // 超级表名
    private String tName; // 表名

    // data
    private String collectedTime;
    private Double dataValue;

    // tags
    private String tagName;
    private String department;
    private String productionLine;
    private String region;
    private String code9;
    // tags
    private String code2;
    private String code4;
    private String code6;
    private String code12;
}
