package com.lestcrm.deepseekcrmsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lestcrm.deepseekcrmsystem.entity.CustomerLabel;
import com.lestcrm.deepseekcrmsystem.entity.Label;
import com.lestcrm.deepseekcrmsystem.mapper.CustomerLabelMapper;
import com.lestcrm.deepseekcrmsystem.mapper.LabelMapper;
import com.lestcrm.deepseekcrmsystem.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelMapper labelMapper;
    private final CustomerLabelMapper customerLabelMapper;

    // 构造器注入（Spring 4.3+ 可以省略 @Autowired）
    public LabelServiceImpl(LabelMapper labelMapper, CustomerLabelMapper customerLabelMapper) {
        this.labelMapper = labelMapper;
        this.customerLabelMapper = customerLabelMapper;
    }

    @Override
    public Page<Label> getLabelPage(Integer page, Integer size, String keyword) {
        Page<Label> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<Label> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Label::getName, keyword).or().like(Label::getCategory, keyword);
        }
        wrapper.orderByAsc(Label::getCategory).orderByAsc(Label::getName);
        return labelMapper.selectPage(pageObj, wrapper);
    }

    @Override
    public List<Label> getAllLabels() {
        return labelMapper.selectList(null);
    }

    @Override
    public boolean saveLabel(Label label) {
        return labelMapper.insert(label) > 0;
    }

    @Override
    public boolean updateLabel(Label label) {
        return labelMapper.updateById(label) > 0;
    }

    @Override
    public boolean deleteLabel(Long id) {
        return labelMapper.deleteById(id) > 0;
    }

    @Override
    public List<Label> getLabelsByCustomerId(Long customerId) {
        return labelMapper.selectLabelsByCustomerId(customerId);
    }

    // ========== 批量查询多个客户的标签 ==========
    @Override
    public Map<Long, List<Label>> getLabelsByCustomerIds(List<Long> customerIds) {
        if (CollectionUtils.isEmpty(customerIds)) {
            return Collections.emptyMap();
        }

        // 1. 查询中间表，获取客户ID -> 标签ID 的关联
        LambdaQueryWrapper<CustomerLabel> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CustomerLabel::getCustomerId, customerIds);
        List<CustomerLabel> relations = customerLabelMapper.selectList(wrapper);

        if (relations.isEmpty()) {
            // 为每个客户 ID 预置空列表，避免无标签的客户在映射结果中缺失
            Map<Long, List<Label>> resultMap = new HashMap<>();
            for (Long id : customerIds) {
                resultMap.put(id, Collections.emptyList());
            }
            return resultMap;
        }

        // 2. 取出所有标签ID
        List<Long> labelIds = relations.stream()
                .map(CustomerLabel::getLabelId)
                .distinct()
                .collect(Collectors.toList());

        // 3. 使用 LambdaQueryWrapper + in 替代已弃用的 selectBatchIds
        LambdaQueryWrapper<Label> labelWrapper = new LambdaQueryWrapper<>();
        labelWrapper.in(Label::getId, labelIds);
        List<Label> labels = labelMapper.selectList(labelWrapper);

        // 4. 转成 Map<标签ID, 标签对象>
        Map<Long, Label> labelMap = labels.stream()
                .collect(Collectors.toMap(Label::getId, Function.identity()));

        // 5. 组装成 Map<客户ID, List<Label>>
        Map<Long, List<Label>> resultMap = relations.stream()
                .collect(Collectors.groupingBy(
                        CustomerLabel::getCustomerId,
                        Collectors.mapping(rel -> labelMap.get(rel.getLabelId()), Collectors.toList())
                ));

        // 6. 确保所有请求的客户ID都有对应的空列表（避免返回 null）
        for (Long id : customerIds) {
            resultMap.putIfAbsent(id, Collections.emptyList());
        }
        return resultMap;
    }
}