package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.artoo.dto.Display;
import org.sopt.artoo.dto.DisplayContent;
import org.sopt.artoo.model.DisplayContentRes;
import org.sopt.artoo.model.DisplayReq;

import java.util.List;

@Mapper
public interface DisplayContentMapper {
    @Select("SELECT * FROM display_content ORDER BY d_sDateNow")
    List<Display> findAll(@Param("d_idx") final int d_idx);

    @Select("SELECT * FROM display_content WHERE d_idx=#{d_idx}")
    List<DisplayContent> findByDisplay(@Param("d_idx") final int d_idx);

    // 전시 컨텐츠 테이블에서 이미 등록된 전시인지 확인
    @Select("SELECT * FROM display_content WHERE u_idx = #{u_idx} and d_idx=#{d_idx}")
    DisplayContent findByUidxAndDidx(@Param("u_idx") final int u_idx, @Param("d_idx") final int d_idx);

    @Select("SELECT * FROM display_content WHERE dc_idx = #{dc_idx}")
    DisplayContent findByDisplayContentIdx(@Param("dc_idx") final int dc_idx);

    @Select("SELECT * FROM display_content WHERE u_idx = #{u_idx}")
    List<DisplayContent> findByUidx(@Param("u_idx") final int u_idx);

    @Select("SELECT  dc.dc_idx, a.a_idx, a.a_name, a.a_width, a.a_height, a.a_depth, a.a_form, a.a_year, a.u_idx FROM artwork a, display_content dc " +
            "WHERE a.a_idx=dc.a_idx and dc.d_idx=#{d_idx} AND a.a_active = 1")
    List<DisplayContentRes> findArtworksByDisplayIdx(@Param("d_idx") final int d_idx);

    @Insert("INSERT INTO display_content(d_idx, a_idx, u_idx, dc_date) VALUES (#{displayReq.d_idx}, #{displayReq.a_idx}, #{displayReq.u_idx},  #{displayReq.dc_date})")
//    @Options(useGeneratedKeys = true, keyProperty = "displayReq.a_idx")
    void save(@Param("displayReq") final DisplayReq displayReq);

    @Delete("DELETE FROM display_content WHERE dc_idx=#{dc_idx}")
    void deleteByDcIdx(@Param("dc_idx") final int dc_idx);

    @Delete("DELETE FROM display_content WHERE u_idx=#{u_idx} and dc_idx=#{dc_idx}")
    void deleteByUidxAndDidx(@Param("u_idx") final int u_idx, @Param("dc_idx") final int dc_idx);

    @Delete("DELETE FROM display_content WHERE a_idx=#{a_idx}")
    void deleteByArtIdx(@Param("a_idx") final int a_idx);

    @Select("SELECT * FROM display_content WHERE a_idx=#{a_idx}")
    DisplayContent findByArtworkIdx(@Param("a_idx") final int a_idx);

    @Delete("DELETE FROM display_content WHERE d_idx=#{d_idx}")
    void deleteByDIsplayIdx(@Param("d_idx") final int d_idx);


    @Select("SELECT count(*) FROM display_content WHERE d_idx=#{d_idx}")
    int findDisplayContentCount(@Param("d_idx") final int d_idx);
}


