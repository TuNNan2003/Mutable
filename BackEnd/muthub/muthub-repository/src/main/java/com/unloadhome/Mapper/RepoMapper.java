package com.unloadhome.Mapper;

import com.unloadhome.common.RepoVisible;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RepoMapper {
    @Select("select name from repo where owner_id = #{userID}")
    public List<String> FindAllRepoName(long userID);

    @Insert("insert into repo values (#{owner_id}, #{name}, #{visible})")
    public void NewRepo(@Param("owner_id") long userID, @Param("name") String name, @Param("visible")RepoVisible visible);

}
