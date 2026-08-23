package com.lestcrm.deepseekcrmsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;   // 改为 LocalDateTime

@Data
@TableName("sys_config")
public class SysConfig {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String configKey;
    private String configValue;
    private LocalDateTime updateTime;  // 使用 LocalDateTime
}