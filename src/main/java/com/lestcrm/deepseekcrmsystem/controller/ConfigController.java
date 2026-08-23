package com.lestcrm.deepseekcrmsystem.controller;

import com.lestcrm.deepseekcrmsystem.entity.SysConfig;
import com.lestcrm.deepseekcrmsystem.mapper.SysConfigMapper;
import com.lestcrm.deepseekcrmsystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @GetMapping("/prompt")
    public Result<String> getPrompt() {
        SysConfig config = sysConfigMapper.selectByKey("ai_eval_prompt");
        return Result.success(config != null ? config.getConfigValue() : "");
    }

    @PutMapping("/prompt")
    public Result<Void> updatePrompt(@RequestBody String prompt) {
        SysConfig config = sysConfigMapper.selectByKey("ai_eval_prompt");
        if (config == null) {
            config = new SysConfig();
            config.setConfigKey("ai_eval_prompt");
        }
        config.setConfigValue(prompt);
        if (config.getId() != null) {
            sysConfigMapper.updateById(config);
        } else {
            sysConfigMapper.insert(config);
        }
        return Result.success(null);
    }
}