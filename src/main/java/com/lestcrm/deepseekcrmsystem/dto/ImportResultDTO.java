package com.lestcrm.deepseekcrmsystem.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 导入结果DTO
 */
@Data
public class ImportResultDTO {
    private int total;              // 总记录数
    private int successCount;       // 成功导入数
    private int failCount;          // 失败数
    private List<ImportError> errors = new ArrayList<>();  // 错误详情

    @Data
    public static class ImportError {
        private int rowNum;         // 行号（Excel中的行号，从1开始）
        private String companyName; // 公司名称（便于定位）
        private String reason;      // 错误原因
    }

    public void addError(int rowNum, String companyName, String reason) {
        ImportError error = new ImportError();
        error.setRowNum(rowNum);
        error.setCompanyName(companyName);
        error.setReason(reason);
        this.errors.add(error);
        this.failCount++;
    }
}
