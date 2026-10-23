package com.lestcrm.deepseekcrmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lestcrm.deepseekcrmsystem.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 统计符合条件的客户总数（去重）
     * labelIds 为多标签组合筛选条件：多个标签时要求客户同时拥有这些标签
     */
    Long countCustomerWithFilters(@Param("keyword") String keyword,
                                  @Param("industry") String industry,
                                  @Param("employeeScale") String employeeScale,
                                  @Param("labelIds") List<Long> labelIds);

    /**
     * 分页查询符合条件的客户ID列表（去重）
     */
    List<Long> selectCustomerIdsByFilters(@Param("keyword") String keyword,
                                          @Param("industry") String industry,
                                          @Param("employeeScale") String employeeScale,
                                          @Param("labelIds") List<Long> labelIds,
                                          @Param("offset") long offset,
                                          @Param("limit") long limit);

    // ========== 看板统计查询 ==========

    /**
     * 获取近30天每天的客户增长数据
     */
    @Select("SELECT DATE(create_time) as date, COUNT(*) as count " +
            "FROM customer " +
            "WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
            "GROUP BY DATE(create_time) " +
            "ORDER BY date")
    List<Map<String, Object>> getCustomerTrend();

    /**
     * 获取各标签分类下各标签名称的客户数量分布
     * 返回格式：[{category: "意向程度", labelName: "高意向", value: 12}, ...]
     */
    @Select("SELECT l.category, l.name as labelName, COUNT(DISTINCT cl.customer_id) as value " +
            "FROM label l " +
            "LEFT JOIN customer_label cl ON l.id = cl.label_id " +
            "GROUP BY l.category, l.name " +
            "ORDER BY l.category, l.name")
    List<Map<String, Object>> getTagDistribution();

    /**
     * 获取各优先级等级的客户数量
     */
    @Select("SELECT ai_priority_level as level, COUNT(*) as count " +
            "FROM customer " +
            "WHERE ai_priority_level IS NOT NULL " +
            "GROUP BY ai_priority_level")
    List<Map<String, Object>> getPriorityDistribution();


}