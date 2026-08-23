package com.lestcrm.deepseekcrmsystem.utils;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private Long total;      // 总记录数
    private List<T> records; // 当前页数据
}
