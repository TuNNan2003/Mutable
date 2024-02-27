package com.unloadhome.Mapper;

import com.unloadhome.model.UserRepoAct;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRepoActMapper {
    @Select("select * from user_repo_act where userId = #{userID} and ownerID = #{ownerID} and repoName = #{repoName}")
    public List<UserRepoAct> CheckEntity(@Param("userID") long userID, @Param("ownerID")long ownerID, @Param("repoName")String repoName);

    @Insert("insert into user_repo_act values (#{userID}, #{ownerID}, #{repoName}, false, false, false)")
    public void InsertEntity(@Param("userID") long userID, @Param("ownerID")long ownerID, @Param("repoName")String repoName);

    @Update("update user_repo_act set star = True where userID = #{userID} and ownerID = #{ownerID} and repoName = #{repoName}")
    public void StarRepo(@Param("userID") long userID, @Param("ownerID")long ownerID, @Param("repoName")String repoName);

    @Update("update user_repo_act set watch = True where userID = #{userID} and ownerID = #{ownerID} and repoName = #{repoName}")
    public void WatchRepo(@Param("userID") long userID, @Param("ownerID")long ownerID, @Param("repoName")String repoName);

    @Update("update user_repo_act set collaborator = True where userID = #{userID} and ownerID = #{ownerID} and repoName = #{repoName}")
    public void CollaborateRepo(@Param("userID") long userID, @Param("ownerID")long ownerID, @Param("repoName")String repoName);

    @Update("update user_repo_act set star = False where userID = #{userID} and ownerID = #{ownerID} and repoName = #{repoName}")
    public void UnStarRepo(@Param("userID") long userID, @Param("ownerID")long ownerID, @Param("repoName")String repoName);

    @Update("update user_repo_act set watch = False where userID = #{userID} and ownerID = #{ownerID} and repoName = #{repoName}")
    public void UnWatchRepo(@Param("userID") long userID, @Param("ownerID")long ownerID, @Param("repoName")String repoName);

    @Update("update user_repo_act set collaborator = False where userID = #{userID} and ownerID = #{ownerID} and repoName = #{repoName}")
    public void UnCollaborateRepo(@Param("userID") long userID, @Param("ownerID")long ownerID, @Param("repoName")String repoName);
}
