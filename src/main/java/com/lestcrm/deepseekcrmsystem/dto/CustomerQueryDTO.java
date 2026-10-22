package com.lestcrm.deepseekcrmsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerQueryDTO {
    private Integer page = 1;       // 默认第1页
    private Integer size = 10;      // 默认每页10条
    private String keyword;         // 搜索关键词（企业名称/联系人/标签名称）
    private String industry;        // 行业筛选
    private String employeeScale;   // 规模筛选

    /**
     * 多标签组合筛选：传入多个标签 ID 时，返回"同时拥有这些标签"的客户。
     * 只传一个时等价于按单个标签过滤；为空表示不按标签过滤。
     */
    private List<Long> labelIds;
}
