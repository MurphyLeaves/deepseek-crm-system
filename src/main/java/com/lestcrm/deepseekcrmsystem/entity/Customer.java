package com.lestcrm.deepseekcrmsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("customer") // 指定表名
public class Customer {
    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;
    private String companyName;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String industry;
    private String employeeScale;
    private String source;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    // ========== 新增：AI 智能评估字段 ==========
    private String aiPriorityLevel;   // A/B/C
    private Integer aiPriorityScore;  // 1-10
    private String aiReason;          // 评估理由
    private LocalDateTime aiEvalTime; // 评估时间
}