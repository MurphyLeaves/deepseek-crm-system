package com.lestcrm.deepseekcrmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lestcrm.deepseekcrmsystem.entity.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    /**
     * 根据配置键查询配置值
     */
    @Select("SELECT * FROM sys_config WHERE config_key = #{key}")
    SysConfig selectByKey(@Param("key") String key);
}