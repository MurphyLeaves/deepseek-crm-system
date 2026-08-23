package com.lestcrm.deepseekcrmsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("label")
public class Label {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String category;
    private String color;
    private LocalDateTime createTime;
}