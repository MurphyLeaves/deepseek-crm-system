package com.lestcrm.deepseekcrmsystem.dto;

import lombok.Data;

@Data
public class CustomerQueryDTO {
    private Integer page = 1;       // 默认第1页
    private Integer size = 10;      // 默认每页10条
    private String keyword;         // 搜索关键词（企业名称/联系人）
    private String industry;        // 行业筛选
    private String employeeScale;   // 规模筛选
}