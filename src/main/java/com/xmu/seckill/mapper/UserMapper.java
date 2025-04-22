package com.xmu.seckill.mapper;

import com.xmu.seckill.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    //@Insert("INSERT INTO user (name, password) VALUES (#{name}, #{password})")
    void insertUser(User user);

    //@Select("SELECT * FROM user WHERE name = #{name}")
    User findByName(String name);

    List<User> selectAll();

    User findById(Long id);
}
