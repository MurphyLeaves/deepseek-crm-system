// CustomerService.java
package com.lestcrm.deepseekcrmsystem.service;

import com.lestcrm.deepseekcrmsystem.dto.CustomerQueryDTO;
import com.lestcrm.deepseekcrmsystem.dto.CustomerSaveDTO;
import com.lestcrm.deepseekcrmsystem.entity.Customer;
import com.lestcrm.deepseekcrmsystem.utils.PageResult;
import com.lestcrm.deepseekcrmsystem.vo.CustomerVO;

import java.util.List;

public interface CustomerService {
    // 🔥 分页查询（返回类型改为 PageResult<CustomerVO>）
    PageResult<CustomerVO> getCustomerPage(CustomerQueryDTO queryDTO);

    // 新增客户
    boolean saveCustomer(CustomerSaveDTO saveDTO);

    // 根据ID查询客户
    Customer getById(Long id);

    // 根据ID查询客户详情（包含已分配标签，用于详情展示与编辑回显）
    CustomerVO getDetailById(Long id);

    // 根据ID删除客户
    boolean deleteById(Long id);

    // 更新客户
    boolean updateCustomer(Long id, CustomerSaveDTO saveDTO);

    // 给客户添加标签
    boolean addLabelsToCustomer(Long customerId, List<Long> labelIds);

    // 移除客户的某个标签
    boolean removeLabelFromCustomer(Long customerId, Long labelId);
}