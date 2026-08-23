package com.lestcrm.deepseekcrmsystem.service;

import com.lestcrm.deepseekcrmsystem.dto.ImportResultDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImportExportService {

    /**
     * 生成导入模板
     */
    byte[] generateTemplate();

    /**
     * 导入客户数据
     */
    ImportResultDTO importCustomers(MultipartFile file) throws Exception;

    /**
     * 导出客户数据
     */
    byte[] exportCustomers(String keyword, String industry, String employeeScale);
}