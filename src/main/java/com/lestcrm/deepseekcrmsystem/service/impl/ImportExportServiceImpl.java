package com.lestcrm.deepseekcrmsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lestcrm.deepseekcrmsystem.dto.ImportResultDTO;
import com.lestcrm.deepseekcrmsystem.entity.Customer;
import com.lestcrm.deepseekcrmsystem.mapper.CustomerMapper;
import com.lestcrm.deepseekcrmsystem.service.ImportExportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportExportServiceImpl implements ImportExportService {

    // 【修改点 1】 改为构造器注入，摒弃字段注入
    private final CustomerMapper customerMapper;

    @Autowired
    public ImportExportServiceImpl(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    // ========== 导入模板列索引 ==========
    private static final int COL_COMPANY_NAME = 0;
    private static final int COL_CONTACT_PERSON = 1;
    private static final int COL_PHONE = 2;
    private static final int COL_EMAIL = 3;
    private static final int COL_ADDRESS = 4;
    private static final int COL_INDUSTRY = 5;
    private static final int COL_EMPLOYEE_SCALE = 6;
    private static final int COL_SOURCE = 7;
    private static final int COL_REMARK = 8;
    private static final int TOTAL_COLS = 9;

    @Override
    public byte[] generateTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("客户导入");

            // 【修改点 2】 抽离重复的表头创建逻辑
            String[] headers = {
                    "企业名称（必填）", "联系人", "电话", "邮箱",
                    "地址", "行业", "企业规模", "客户来源", "备注"
            };
            initSheetHeader(sheet, headers);

            // 添加示例数据行（帮助用户理解格式）
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(COL_COMPANY_NAME).setCellValue("示例公司名称");
            exampleRow.createCell(COL_CONTACT_PERSON).setCellValue("张三");
            exampleRow.createCell(COL_PHONE).setCellValue("13800138000");
            exampleRow.createCell(COL_EMAIL).setCellValue("zhangsan@example.com");
            exampleRow.createCell(COL_ADDRESS).setCellValue("浙江省杭州市");
            exampleRow.createCell(COL_INDUSTRY).setCellValue("互联网");
            exampleRow.createCell(COL_EMPLOYEE_SCALE).setCellValue("50-200人");
            exampleRow.createCell(COL_SOURCE).setCellValue("线上推广");
            exampleRow.createCell(COL_REMARK).setCellValue("优质客户");


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("生成模板失败", e);
        }
    }

    @Override
    public ImportResultDTO importCustomers(MultipartFile file) throws Exception {
        ImportResultDTO result = new ImportResultDTO();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new RuntimeException("Excel文件为空");
            }

            // 跳过表头（第0行），从第1行开始读取
            int totalRows = sheet.getPhysicalNumberOfRows();
            List<Customer> customers = new ArrayList<>();

            for (int rowNum = 1; rowNum < totalRows; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;

                // 检查是否全空行
                boolean isEmptyRow = true;
                for (int i = 0; i < TOTAL_COLS; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null && !cell.toString().trim().isEmpty()) {
                        isEmptyRow = false;
                        break;
                    }
                }
                if (isEmptyRow) continue;

                // 解析行数据
                String companyName = getCellString(row, COL_COMPANY_NAME);
                String contactPerson = getCellString(row, COL_CONTACT_PERSON);
                String phone = getCellString(row, COL_PHONE);
                String email = getCellString(row, COL_EMAIL);
                String address = getCellString(row, COL_ADDRESS);
                String industry = getCellString(row, COL_INDUSTRY);
                String employeeScale = getCellString(row, COL_EMPLOYEE_SCALE);
                String source = getCellString(row, COL_SOURCE);
                String remark = getCellString(row, COL_REMARK);

                // ---- 校验 ----
                // 必填：企业名称
                if (!StringUtils.hasText(companyName)) {
                    result.addError(rowNum + 1, "未填写", "企业名称不能为空");
                    continue;
                }

                // 手机号格式校验（非必填，但填写了就要校验）
                if (StringUtils.hasText(phone) && !phone.matches("^1[3-9]\\d{9}$")) {
                    result.addError(rowNum + 1, companyName, "手机号格式不正确，应为11位数字");
                    continue;
                }

                // 邮箱格式校验（非必填，但填写了就要校验）
                if (StringUtils.hasText(email) && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    result.addError(rowNum + 1, companyName, "邮箱格式不正确");
                    continue;
                }

                // 检查是否已存在相同公司名称
                LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Customer::getCompanyName, companyName);
                Long existCount = customerMapper.selectCount(wrapper);
                if (existCount > 0) {
                    result.addError(rowNum + 1, companyName, "该公司名称已存在，请勿重复导入");
                    continue;
                }

                // ---- 构造Customer对象 ----
                Customer customer = new Customer();
                customer.setCompanyName(companyName);
                customer.setContactPerson(contactPerson);
                customer.setPhone(phone);
                customer.setEmail(email);
                customer.setAddress(address);
                customer.setIndustry(industry);
                customer.setEmployeeScale(employeeScale);
                customer.setSource(source);
                customer.setRemark(remark);
                customers.add(customer);
            }

            // ---- 批量导入 ----
            result.setTotal(customers.size() + result.getFailCount());
            if (!customers.isEmpty()) {
                for (Customer customer : customers) {
                    customerMapper.insert(customer);
                    result.setSuccessCount(result.getSuccessCount() + 1);
                }
            }

            // 重新计算总记录数
            result.setTotal(result.getSuccessCount() + result.getFailCount());

        } catch (Exception e) {
            throw new Exception("解析Excel文件失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public byte[] exportCustomers(String keyword, String industry, String employeeScale) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("客户数据");

            // 【修改点 2】 再次调用抽离的表头创建逻辑，完全复用，杜绝重复代码
            String[] headers = {
                    "企业名称", "联系人", "电话", "邮箱", "地址",
                    "行业", "企业规模", "客户来源", "备注",
                    "AI等级", "AI评分", "AI理由", "AI评估时间"
            };
            initSheetHeader(sheet, headers);

            // ---- 查询客户数据 ----
            LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasText(keyword)) {
                wrapper.and(w -> w
                        .like(Customer::getCompanyName, keyword)
                        .or()
                        .like(Customer::getContactPerson, keyword)
                );
            }
            if (StringUtils.hasText(industry)) {
                wrapper.eq(Customer::getIndustry, industry);
            }
            if (StringUtils.hasText(employeeScale)) {
                wrapper.eq(Customer::getEmployeeScale, employeeScale);
            }
            wrapper.orderByDesc(Customer::getCreateTime);

            List<Customer> customers = customerMapper.selectList(wrapper);

            // ---- 填充数据 ----
            int rowNum = 1;
            for (Customer c : customers) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(c.getCompanyName() != null ? c.getCompanyName() : "");
                row.createCell(1).setCellValue(c.getContactPerson() != null ? c.getContactPerson() : "");
                row.createCell(2).setCellValue(c.getPhone() != null ? c.getPhone() : "");
                row.createCell(3).setCellValue(c.getEmail() != null ? c.getEmail() : "");
                row.createCell(4).setCellValue(c.getAddress() != null ? c.getAddress() : "");
                row.createCell(5).setCellValue(c.getIndustry() != null ? c.getIndustry() : "");
                row.createCell(6).setCellValue(c.getEmployeeScale() != null ? c.getEmployeeScale() : "");
                row.createCell(7).setCellValue(c.getSource() != null ? c.getSource() : "");
                row.createCell(8).setCellValue(c.getRemark() != null ? c.getRemark() : "");
                row.createCell(9).setCellValue(c.getAiPriorityLevel() != null ? c.getAiPriorityLevel() : "");
                row.createCell(10).setCellValue(c.getAiPriorityScore() != null ? c.getAiPriorityScore().toString() : "");
                row.createCell(11).setCellValue(c.getAiReason() != null ? c.getAiReason() : "");

                String evalTime = "";
                if (c.getAiEvalTime() != null) {
                    evalTime = c.getAiEvalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                row.createCell(12).setCellValue(evalTime);
            }


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("导出客户数据失败", e);
        }
    }

    // ========== 工具方法 ==========

    /**
     * 抽离出的公共方法，用于初始化 Excel 的表头、样式和列宽
     */
    private void initSheetHeader(Sheet sheet, String[] headers) {
        Workbook workbook = sheet.getWorkbook();

        // 创建表头样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 加上边框（合并了两处代码不同的微妙之处，直接用最全的）
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 4000);
        }
    }

    private String getCellString(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;
        return cell.toString().trim();
    }
}