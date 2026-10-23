package com.lestcrm.deepseekcrmsystem.controller;

import com.lestcrm.deepseekcrmsystem.dto.CustomerQueryDTO;
import com.lestcrm.deepseekcrmsystem.dto.CustomerSaveDTO;
import com.lestcrm.deepseekcrmsystem.entity.Customer;
import com.lestcrm.deepseekcrmsystem.service.CustomerService;
import com.lestcrm.deepseekcrmsystem.utils.PageResult;
import com.lestcrm.deepseekcrmsystem.utils.Result;
import com.lestcrm.deepseekcrmsystem.vo.CustomerVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    // 构造器注入（Spring 4.3+ 可省略 @Autowired）
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 分页查询客户列表（返回带标签的数据）
     * GET /api/customers/page?page=1&size=10&keyword=云创
     */
    @GetMapping("/page")
    public Result<PageResult<CustomerVO>> getPage(CustomerQueryDTO queryDTO) {
        PageResult<CustomerVO> pageResult = customerService.getCustomerPage(queryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增客户
     * POST /api/customers
     */
    @PostMapping
    public Result<Boolean> save(@RequestBody CustomerSaveDTO saveDTO) {
        boolean success = customerService.saveCustomer(saveDTO);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("保存客户失败");
        }
    }

    /**
     * 根据ID查询客户详情（含已分配标签，用于详情展示与编辑回显）
     * GET /api/customers/{id}
     */
    @GetMapping("/{id}")
    public Result<CustomerVO> getById(@PathVariable Long id) {
        CustomerVO customer = customerService.getDetailById(id);
        if (customer == null) {
            return Result.error("客户不存在");
        }
        return Result.success(customer);
    }

    /**
     * 删除客户
     * DELETE /api/customers/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean success = customerService.deleteById(id);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("删除失败，该客户可能不存在");
        }
    }

    /**
     * 更新客户信息
     * PUT /api/customers/{id}
     */
    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody CustomerSaveDTO saveDTO) {
        boolean success = customerService.updateCustomer(id, saveDTO);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("更新失败，该客户可能不存在");
        }
    }

    /**
     * 为客户添加标签（批量）
     * POST /api/customers/{customerId}/labels
     * 请求体：["标签ID1", "标签ID2", ...]
     */
    @PostMapping("/{customerId}/labels")
    public Result<Boolean> addLabelsToCustomer(@PathVariable Long customerId,
                                               @RequestBody List<Long> labelIds) {
        boolean success = customerService.addLabelsToCustomer(customerId, labelIds);
        return Result.success(success);
    }

    /**
     * 移除客户的某个标签
     * DELETE /api/customers/{customerId}/labels/{labelId}
     */
    @DeleteMapping("/{customerId}/labels/{labelId}")
    public Result<Boolean> removeLabelFromCustomer(@PathVariable Long customerId,
                                                   @PathVariable Long labelId) {
        boolean success = customerService.removeLabelFromCustomer(customerId, labelId);
        return Result.success(success);
    }
}