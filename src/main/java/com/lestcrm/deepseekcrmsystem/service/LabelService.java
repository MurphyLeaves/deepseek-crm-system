package com.lestcrm.deepseekcrmsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lestcrm.deepseekcrmsystem.entity.Label;
import java.util.List;
import java.util.Map;

public interface LabelService {
    // 分页查询标签
    Page<Label> getLabelPage(Integer page, Integer size, String keyword);

    // 查询所有标签（用于下拉列表）
    List<Label> getAllLabels();

    // 新增标签
    boolean saveLabel(Label label);

    // 更新标签
    boolean updateLabel(Label label);

    // 删除标签
    boolean deleteLabel(Long id);

    // 根据客户ID获取该客户的标签
    List<Label> getLabelsByCustomerId(Long customerId);

    /**
     * 根据客户ID列表，批量查询每个客户的标签
     * @param customerIds 客户ID列表
     * @return Map<客户ID, 该客户的标签列表>
     */
    Map<Long, List<Label>> getLabelsByCustomerIds(List<Long> customerIds);
}