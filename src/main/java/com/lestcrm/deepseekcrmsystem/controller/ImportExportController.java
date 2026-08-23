package com.lestcrm.deepseekcrmsystem.controller;

import com.lestcrm.deepseekcrmsystem.dto.ImportResultDTO;
import com.lestcrm.deepseekcrmsystem.service.ImportExportService;
import com.lestcrm.deepseekcrmsystem.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/import-export")
public class ImportExportController {

    // 【改进点 1】 添加日志记录器
    private static final Logger log = LoggerFactory.getLogger(ImportExportController.class);

    // 【改进点 2】 使用构造器注入 (代替 @Autowired 字段注入)
    private final ImportExportService importExportService;

    // 如果项目中引入了 Lombok，可以使用 @RequiredArgsConstructor 代替下面这个构造函数。
    // 在 Spring 4.3+ 中，单构造器可以省略 @Autowired 注解，但加上更稳妥。
    @Autowired
    public ImportExportController(ImportExportService importExportService) {
        this.importExportService = importExportService;
    }

    /**
     * 1. 下载导入模板
     */
    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        byte[] bytes = importExportService.generateTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String filename = URLEncoder.encode("客户导入模板.xlsx", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + filename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }

    /**
     * 2. 导入Excel
     */
    @PostMapping("/import")
    public Result<ImportResultDTO> importCustomers(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("上传文件为空");
            }
            // 检查文件格式
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !(originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".xls"))) {
                return Result.error("请上传Excel文件（.xlsx或.xls格式）");
            }
            ImportResultDTO result = importExportService.importCustomers(file);
            return Result.success(result);
        } catch (Exception e) {
            // 【改进点 3】 用日志记录器替换 e.printStackTrace()
            // 参数化日志，记录详细异常栈，但是不直接返回给前端
            log.error("导入客户数据失败, 文件名: {}, 错误信息: ", file.getOriginalFilename(), e);

            // 返回给前端的提示应该避免抛出具体的报错栈，防止安全漏洞或暴露内部路径
            return Result.error("导入失败，请检查文件格式或联系管理员");
        }
    }

    /**
     * 3. 导出客户数据
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCustomers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String employeeScale) {

        byte[] bytes = importExportService.exportCustomers(keyword, industry, employeeScale);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String filename = URLEncoder.encode("客户数据导出.xlsx", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + filename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }
}