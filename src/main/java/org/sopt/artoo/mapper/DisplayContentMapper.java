package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.artoo.dto.DisplayContent;
import org.sopt.artoo.model.DisplayReq;

@Mapper
public interface DisplayContentMapper {
    @Select("SELECT * FROM display_content WHERE a_idx = #{a_idx}")
    DisplayContent findByArtworkIdx(@Param("a_idx") final int a_idx);

    @Insert("INSERT INTO display_content(d_idx, a_idx, u_idx) VALUES(#{displayReq.d_idx}, #{displayReq.a_idx}, #{displayReq.u_idx})")
    void save(@Param("displayReq") final DisplayReq displayReq);

    @Delete("DELETE FROM display_content WHERE dc_idx=#{dc_idx}")
    void delete(@Param("dc_idx") final int dc_idx);
}
