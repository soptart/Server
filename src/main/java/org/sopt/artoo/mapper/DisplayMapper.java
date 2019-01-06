package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.artoo.dto.Display;
import org.sopt.artoo.dto.DisplayContent;
import org.sopt.artoo.model.DisplayAddReq;

import java.util.List;

@Mapper
public interface DisplayMapper {

    @Select("SELECT * FROM display")
    List<Display> findAllDisplay();

    @Select("SELECT * FROM display WHERE d_idx=#{d_idx}")
    Display findByDisplayidx(@Param("d_idx") final int d_idx);

    @Delete("DELETE FROM display WHERE d_idx=#{d_idx}")
    void deleteByDisplayIdx(@Param("d_idx") final int d_idx);

    /**
     * 전시 추가 - 관리자가
     * 추가는 됨 but Server Error가 남
     */
    @Insert("INSERT INTO display( d_idx,  d_repImg_url, d_title, d_longDetail, d_sDateApply, d_eDateApply, " +
            "d_sDateNow, d_eDateNow, d_titleImg_url, d_mainImg_url, d_shortDetail, d_subTitle) " +
            "VALUES ( #{displayAddReq.d_idx}, #{displayAddReq.d_repImg_url}, #{displayAddReq.d_title}, #{displayAddReq.d_longDetail}, " +
            "#{displayAddReq.d_sDateApply}, #{displayAddReq.d_eDateApply}, " +
            "#{displayAddReq.d_sDateNow}, #{displayAddReq.d_eDateNow}," +
            "#{displayAddReq.d_titleImg_url}, #{displayAddReq.d_mainImg_url}, #{displayAddReq.d_shortDetail}, #{displayAddReq.d_subTitle})")
    @Options(useGeneratedKeys = true, keyProperty = "display.d_idx")
    void addDisplay(@Param("displayAddReq") final DisplayAddReq displayAddReq);


    /**
     * 전시 수정 - 관리자가
     */
    @Update("UPDATE display SET d_repImg_url = #{displayAddReq.d_repImg_url}, d_title = #{displayAddReq.d_title}, " +
            "d_longDetail = #{displayAddReq.d_longDetail} ,d_sDateApply= #{displayAddReq.d_sDateApply}, " +
            "d_eDateApply= #{displayAddReq.d_eDateApply}, d_sDateNow=#{displayAddReq.d_sDateNow}, " +
            "d_eDateNow= #{displayAddReq.d_eDateNow}, d_titleImg_url=#{displayAddReq.d_titleImg_url}, " +
            "d_mainImg_url=#{displayAddReq.d_mainImg_url}, d_shortDetail=#{displayAddReq.d_shortDetail}, " +
            "d_subTitle=#{displayAddReq.d_subTitle} WHERE d_idx = #{displayAddReq.d_idx}")
    void updateDisplay(@Param("displayAddReq") final DisplayAddReq displayAddReq);
}