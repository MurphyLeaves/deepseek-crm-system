package com.lestcrm.deepseekcrmsystem.vo;

import com.lestcrm.deepseekcrmsystem.entity.Customer;
import com.lestcrm.deepseekcrmsystem.entity.Label;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerVO extends Customer {
    private List<Label> labels;  // 客户的标签列表
}