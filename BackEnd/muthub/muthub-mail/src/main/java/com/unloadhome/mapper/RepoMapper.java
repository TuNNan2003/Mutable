package com.unloadhome.mapper;

import com.unloadhome.model.Repo;
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
    public void NewRepo(@Param("owner_id") long userID, @Param("name") String name, @Param("visible")String visible);

    @Select("select visible from repo where owner_id = #{ownerID} and name = #{repoName}")
    public String getVisible(@Param("ownerID")long userId, @Param("repoName")String repoName);

    @Select("select * from repo where owner_id = #{ownerID} and name = #{repoName}")
    public List<Repo> getRepo(@Param("ownerID")long userId, @Param("repoName")String repoName);
}
