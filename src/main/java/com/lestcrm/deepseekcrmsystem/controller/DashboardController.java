package com.lestcrm.deepseekcrmsystem.controller;

import com.lestcrm.deepseekcrmsystem.dto.DashboardDTO;
import com.lestcrm.deepseekcrmsystem.entity.Customer;
import com.lestcrm.deepseekcrmsystem.mapper.CustomerMapper;
import com.lestcrm.deepseekcrmsystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping("/stats")
    public Result<DashboardDTO> getDashboardStats() {
        DashboardDTO dto = new DashboardDTO();

        // 1. 统计卡片
        Long total = customerMapper.selectCount(null);
        dto.setTotalCustomers(total);

        // 本月新增
        Long monthNew = customerMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.lestcrm.deepseekcrmsystem.entity.Customer>()
                        .apply("DATE_FORMAT(create_time, '%Y-%m') = DATE_FORMAT(CURDATE(), '%Y-%m')")
        );
        dto.setMonthNewCustomers(monthNew);

        // A级客户数
        Long levelACount = customerMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.lestcrm.deepseekcrmsystem.entity.Customer>()
                        .eq(Customer::getAiPriorityLevel, "A")
        );
        dto.setLevelACount(levelACount);

        // 2. 客户增长趋势（近30天）
        List<Map<String, Object>> trendData = customerMapper.getCustomerTrend();
        // 补全近30天所有日期（无数据的填0）
        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        LocalDate today = LocalDate.now();
        Map<String, Long> dateCountMap = trendData.stream()
                .collect(Collectors.toMap(
                        m -> m.get("date").toString(),
                        m -> ((Number) m.get("count")).longValue()
                ));
        for (int i = 29; i >= 0; i--) {
            String dateStr = today.minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dates.add(dateStr);
            counts.add(dateCountMap.getOrDefault(dateStr, 0L));
        }
        dto.setDates(dates);
        dto.setCustomerCounts(counts);

        // 3. 标签分布
        List<Map<String, Object>> tagData = customerMapper.getTagDistribution();
        // 过滤掉空分类（按 category 过滤）
        tagData = tagData.stream()
                .filter(m -> m.get("category") != null && !m.get("category").toString().isEmpty())
                .collect(Collectors.toList());
        dto.setTagDistribution(tagData);

        // 4. 优先级分布
        List<Map<String, Object>> priorityData = customerMapper.getPriorityDistribution();
        Map<String, Long> priorityMap = new HashMap<>();
        priorityMap.put("A", 0L);
        priorityMap.put("B", 0L);
        priorityMap.put("C", 0L);
        for (Map<String, Object> row : priorityData) {
            String level = row.get("level").toString();
            Long count = ((Number) row.get("count")).longValue();
            priorityMap.put(level, count);
        }
        dto.setPriorityDistribution(priorityMap);

        return Result.success(dto);
    }
}