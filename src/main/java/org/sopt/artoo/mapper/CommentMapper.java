package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.artoo.dto.Comment;
import org.sopt.artoo.model.CommentReq;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("SELECT * FROM comment")
    List<Comment> findAllComments();

    @Select("SELECT * FROM comment WHERE c_idx = #{c_idx}")
    Comment findCommentByCommentIdx(@Param("c_idx") final int c_idx);

    @Select("SELECT * FROM comment WHERE a_idx = #{a_idx}")
    List<Comment> findAllCommentByArtIdx(@Param("a_idx") final int a_idx);

    @Select("SELECT * FROM comment WHERE u_idx = #{u_idx}")
    List<Comment> findAllCommentByUserIdx(@Param("u_idx") final int u_idx);

    @Insert("INSERT INTO comment(c_idx, c_content, c_date, u_idx, a_idx) VALUES(#{commentReq.c_idx}, #{commentReq.c_content}, #{commentReq.c_date}, #{commentReq.u_idx}, #{commentReq.a_idx})")
    void saveComment(@Param("commentReq") final CommentReq commentReq);

    @Update("UPDATE comment SET c_content = #{commentReq.c_content}, c_date = #{commentReq.c_date} WHERE c_idx = #{commentReq.c_idx}")
    void updateComment(@Param("commentReq") final CommentReq commentReq);

    @Delete("DELETE FROM comment WHERE c_idx = #{c_idx}")
    void deleteCommentByCommentIdx(@Param("c_idx") final int c_idx);

    @Delete("DELETE FROM comment WHERE a_idx = #{a_idx}")
    void deleteByArtIdx(@Param("a_idx") final int a_idx);


}
