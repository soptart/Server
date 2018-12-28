package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.artoo.dto.DisplayContent;
import org.sopt.artoo.model.DisplayContentRes;
import org.sopt.artoo.model.DisplayReq;

import java.util.List;

@Mapper
public interface DisplayContentMapper {
    // 전시 컨텐츠 테이블에서 이미 등록된 전시인지 확인
    @Select("SELECT * FROM display_content WHERE a_idx = #{displayReq.a_idx} and d_idx = #{displayReq.d_idx}")
    DisplayContent findByArtwork(@Param("displayReq") final DisplayReq displayReq);

    @Select("SELECT * FROM display_content WHERE dc_idx = #{dc_idx}")
    DisplayContent findByDisplayContentIdx(@Param("dc_idx") final int dc_idx);

    @Select("SELECT  a.a_idx, a.a_name, a.a_width, a.a_height, a.a_depth, a.a_form, a.a_year, a.u_idx FROM artwork a, display_content dc " +
            "WHERE a.a_idx=dc.a_idx and dc.d_idx=#{d_idx}")
    List<DisplayContentRes> findArtworksByDisplayIdx(@Param("d_idx") final int d_idx);

    @Insert("INSERT INTO display_content(d_idx, a_idx, u_idx) VALUES (#{displayReq.d_idx}, #{displayReq.a_idx}, #{displayReq.u_idx})")
    @Options(useGeneratedKeys = true, keyProperty = "displayReq.a_idx")
    int save(@Param("displayReq") final DisplayReq displayReq);

    @Delete("DELETE FROM display_content WHERE dc_idx=#{dc_idx}")
    void delete(@Param("dc_idx") final int dc_idx);
}
