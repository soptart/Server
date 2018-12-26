package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT u_name FROM user WHERE u_idx = #{userIdx}")
    String findUnameByUidx(@Param("userIdx") final int userIdx);
}
