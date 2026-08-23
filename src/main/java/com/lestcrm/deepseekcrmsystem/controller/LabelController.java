package com.lestcrm.deepseekcrmsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lestcrm.deepseekcrmsystem.entity.Label;
import com.lestcrm.deepseekcrmsystem.service.LabelService;
import com.lestcrm.deepseekcrmsystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    @Autowired
    private LabelService labelService;

    // ---------- 标签库管理 ----------

    @GetMapping("/page")
    public Result<Page<Label>> getPage(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String keyword) {
        Page<Label> result = labelService.getLabelPage(page, size, keyword);
        return Result.success(result);
    }

    @GetMapping("/all")
    public Result<List<Label>> getAll() {
        return Result.success(labelService.getAllLabels());
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody Label label) {
        // 简单校验
        if (label.getName() == null || label.getName().trim().isEmpty()) {
            return Result.error("标签名称不能为空");
        }
        if (label.getCategory() == null || label.getCategory().trim().isEmpty()) {
            return Result.error("标签分类不能为空");
        }
        return Result.success(labelService.saveLabel(label));
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody Label label) {
        label.setId(id);
        return Result.success(labelService.updateLabel(label));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(labelService.deleteLabel(id));
    }

    // ---------- 客户打标签 ----------

    @GetMapping("/customer/{customerId}")
    public Result<List<Label>> getLabelsByCustomer(@PathVariable Long customerId) {
        return Result.success(labelService.getLabelsByCustomerId(customerId));
    }
}