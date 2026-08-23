package com.lestcrm.deepseekcrmsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("customer_label")
public class CustomerLabel {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private Long labelId;
    private LocalDateTime createTime;
}