package com.lestcrm.deepseekcrmsystem.dto;

import lombok.Data;

@Data
public class CustomerSaveDTO {
    private Long id; // 编辑时传id，新增时不传
    private String companyName;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String industry;
    private String employeeScale;
    private String source;
    private String remark;
}