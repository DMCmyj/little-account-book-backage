package com.catpudding.android_back_stage.bill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Description 账单
 * @Author 马永杰
 *
 */
@Data
@TableName("my_bill")
public class BillItem {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "describe_bill")
    private String describeBill;
    @TableField(value = "userId")
    private String userId;
    @TableField(value = "price")
    private Double price;
    @TableField(value = "price_type")
    private Integer priceType;
    @TableField(value = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDate;
    @TableField(value = "type")
    private Integer type;
}
