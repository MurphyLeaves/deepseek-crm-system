package com.lestcrm.deepseekcrmsystem.controller;

import com.lestcrm.deepseekcrmsystem.entity.SysConfig;
import com.lestcrm.deepseekcrmsystem.mapper.SysConfigMapper;
import com.lestcrm.deepseekcrmsystem.utils.AuthUtils;
import com.lestcrm.deepseekcrmsystem.utils.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置与提示词管理
 *
 * 提示词直接决定 AI 评估的结果口径，属于管理员级配置，
 * 因此读取与修改都限制为管理员操作。
 */
@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    /**
     * 读取当前 AI 评估提示词（仅管理员）
     */
    @GetMapping("/prompt")
    public Result<String> getPrompt(HttpSession session) {
        if (!AuthUtils.isAdmin(session)) {
            return Result.forbidden("该功能仅管理员可用");
        }
        SysConfig config = sysConfigMapper.selectByKey("ai_eval_prompt");
        return Result.success(config != null ? config.getConfigValue() : "");
    }

    /**
     * 更新 AI 评估提示词（仅管理员）
     * 配置项存在则更新、不存在则新建，多次调用不会产生重复记录
     */
    @PutMapping("/prompt")
    public Result<Void> updatePrompt(@RequestBody String prompt, HttpSession session) {
        if (!AuthUtils.isAdmin(session)) {
            return Result.forbidden("该功能仅管理员可用");
        }
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
