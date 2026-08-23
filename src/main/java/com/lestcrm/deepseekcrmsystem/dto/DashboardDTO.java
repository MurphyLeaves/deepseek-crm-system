package com.lestcrm.deepseekcrmsystem.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DashboardDTO {
    // 客户增长趋势数据
    private List<String> dates;           // 日期列表，如 ["07-01", "07-02", ...]
    private List<Long> customerCounts;    // 对应日期的客户数量

    // 标签分布数据
    private List<Map<String, Object>> tagDistribution;  // [{name: "高意向", value: 12}, ...]

    // 优先级分布数据
    private Map<String, Long> priorityDistribution;     // {"A": 5, "B": 8, "C": 3}

    // 统计卡片
    private Long totalCustomers;
    private Long monthNewCustomers;
    private Long levelACount;
}
