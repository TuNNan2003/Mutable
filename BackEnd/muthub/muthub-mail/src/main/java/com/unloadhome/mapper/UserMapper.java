package com.unloadhome.mapper;

import com.unloadhome.model.userInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    /*
     * 检查email是否已经在数据库中存在，即已被注册过
     * @return
     *   true -> 已注册
     *   false-> 未注册
     * */
    @Select("select * from user where email = #{email}")
    public List<userInfo> checkEmailExist(String email);

    @Select("select * from user where id = #{userID}")
    public List<userInfo> checkIdExist(long userID);

    @Select("select * from user where id = #{userID}")
    public userInfo selectUser(long userID);

    @Insert("insert into user values (#{id}, #{name}, #{password}, #{email})")
    public void register(userInfo info);

    @Select("select * from user where email = #{email} and password = #{password}")
    public List<userInfo> login(@Param("email")String email, @Param("password") String password);
}