package com.lestcrm.deepseekcrmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lestcrm.deepseekcrmsystem.entity.Label;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface LabelMapper extends BaseMapper<Label> {

    // 根据客户ID查询该客户已拥有的所有标签
    @Select("SELECT l.* FROM label l " +
            "INNER JOIN customer_label cl ON l.id = cl.label_id " +
            "WHERE cl.customer_id = #{customerId}")
    List<Label> selectLabelsByCustomerId(Long customerId);
}