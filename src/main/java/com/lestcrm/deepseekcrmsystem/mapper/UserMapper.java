package com.lestcrm.deepseekcrmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lestcrm.deepseekcrmsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 根据用户名查询用户
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);
}