package com.lestcrm.deepseekcrmsystem.controller;

import com.lestcrm.deepseekcrmsystem.dto.CustomerEvaluationDTO;
import com.lestcrm.deepseekcrmsystem.service.DeepSeekService;
import com.lestcrm.deepseekcrmsystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluation")
public class EvaluationController {

    @Autowired
    private DeepSeekService deepSeekService;

    /**
     * AI评估接口
     * @param customerId 客户ID
     * @param refresh 是否强制刷新（前端传 true 表示重新生成）
     */
    @PostMapping("/{customerId}")
    public Result<CustomerEvaluationDTO> evaluate(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "false") boolean refresh) {
        try {
            CustomerEvaluationDTO result = deepSeekService.evaluateCustomer(customerId, refresh);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("AI评估失败：" + e.getMessage());
        }
    }
}