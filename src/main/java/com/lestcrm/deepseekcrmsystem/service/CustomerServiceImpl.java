package com.lestcrm.deepseekcrmsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lestcrm.deepseekcrmsystem.dto.CustomerQueryDTO;
import com.lestcrm.deepseekcrmsystem.dto.CustomerSaveDTO;
import com.lestcrm.deepseekcrmsystem.entity.Customer;
import com.lestcrm.deepseekcrmsystem.entity.CustomerLabel;
import com.lestcrm.deepseekcrmsystem.entity.Label;
import com.lestcrm.deepseekcrmsystem.mapper.CustomerLabelMapper;
import com.lestcrm.deepseekcrmsystem.mapper.CustomerMapper;
import com.lestcrm.deepseekcrmsystem.service.LabelService;
import com.lestcrm.deepseekcrmsystem.utils.PageResult;
import com.lestcrm.deepseekcrmsystem.vo.CustomerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerLabelMapper customerLabelMapper;
    private final LabelService labelService;   // 🔥 注入

    public CustomerServiceImpl(CustomerMapper customerMapper,
                               CustomerLabelMapper customerLabelMapper,
                               LabelService labelService) {
        this.customerMapper = customerMapper;
        this.customerLabelMapper = customerLabelMapper;
        this.labelService = labelService;
    }

    @Override
    public PageResult<CustomerVO> getCustomerPage(CustomerQueryDTO queryDTO) {
        // 1. 查询总数
        Long total = customerMapper.countCustomerWithFilters(
                queryDTO.getKeyword(),
                queryDTO.getIndustry(),
                queryDTO.getEmployeeScale(),
                queryDTO.getLabelIds()
        );

        // 2. 计算偏移量
        long offset = (queryDTO.getPage() - 1) * queryDTO.getSize();
        // 3. 查询当前页的客户ID列表（已去重、排序、分页）
        List<Long> ids = customerMapper.selectCustomerIdsByFilters(
                queryDTO.getKeyword(),
                queryDTO.getIndustry(),
                queryDTO.getEmployeeScale(),
                queryDTO.getLabelIds(),
                offset,
                queryDTO.getSize()
        );

        List<CustomerVO> voList = Collections.emptyList();
        if (!ids.isEmpty()) {
            // 4. 根据ID批量查询客户详情（保持ids顺序）
            List<Customer> customers = customerMapper.selectBatchIds(ids);
            // 按 ids 顺序重新排序
            Map<Long, Customer> customerMap = customers.stream()
                    .collect(Collectors.toMap(Customer::getId, Function.identity()));
            List<Customer> sortedCustomers = ids.stream()
                    .map(customerMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // 5. 批量查询这些客户的所有标签（无条件）
            Map<Long, List<Label>> labelMap = labelService.getLabelsByCustomerIds(ids);

            // 6. 组装 CustomerVO
            voList = sortedCustomers.stream().map(customer -> {
                CustomerVO vo = new CustomerVO();
                BeanUtils.copyProperties(customer, vo);
                vo.setLabels(labelMap.getOrDefault(customer.getId(), Collections.emptyList()));
                return vo;
            }).collect(Collectors.toList());
        }

        // 7. 封装返回结果
        PageResult<CustomerVO> result = new PageResult<>();
        result.setTotal(total);
        result.setRecords(voList);
        return result;
    }

    // ========== 其他方法保持不变 ==========

    @Override
    public boolean saveCustomer(CustomerSaveDTO saveDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(saveDTO, customer);
        if (saveDTO.getId() == null) {
            return customerMapper.insert(customer) > 0;
        } else {
            return customerMapper.updateById(customer) > 0;
        }
    }

    @Override
    public Customer getById(Long id) {
        return customerMapper.selectById(id);
    }

    @Override
    public CustomerVO getDetailById(Long id) {
        Customer customer = customerMapper.selectById(id);
        if (customer == null) {
            return null;
        }
        CustomerVO vo = new CustomerVO();
        BeanUtils.copyProperties(customer, vo);
        // 详情需要展示该客户已分配的标签
        vo.setLabels(labelService.getLabelsByCustomerId(id));
        return vo;
    }

    @Override
    public boolean deleteById(Long id) {
        return customerMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateCustomer(Long id, CustomerSaveDTO saveDTO) {
        Customer customer = customerMapper.selectById(id);
        if (customer == null) {
            return false;
        }
        customer.setCompanyName(saveDTO.getCompanyName());
        customer.setContactPerson(saveDTO.getContactPerson());
        customer.setPhone(saveDTO.getPhone());
        customer.setEmail(saveDTO.getEmail());
        customer.setAddress(saveDTO.getAddress());
        customer.setIndustry(saveDTO.getIndustry());
        customer.setEmployeeScale(saveDTO.getEmployeeScale());
        customer.setSource(saveDTO.getSource());
        customer.setRemark(saveDTO.getRemark());
        return customerMapper.updateById(customer) > 0;
    }

    @Override
    public boolean addLabelsToCustomer(Long customerId, List<Long> labelIds) {
        if (labelIds == null || labelIds.isEmpty()) {
            return true;
        }
        for (Long labelId : labelIds) {
            CustomerLabel cl = new CustomerLabel();
            cl.setCustomerId(customerId);
            cl.setLabelId(labelId);
            try {
                customerLabelMapper.insert(cl);
            } catch (Exception e) {
                // 忽略重复插入
            }
        }
        return true;
    }

    @Override
    public boolean removeLabelFromCustomer(Long customerId, Long labelId) {
        LambdaQueryWrapper<CustomerLabel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerLabel::getCustomerId, customerId)
                .eq(CustomerLabel::getLabelId, labelId);
        return customerLabelMapper.delete(wrapper) > 0;
    }
}