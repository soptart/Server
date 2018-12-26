package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.artoo.dto.Display;
import org.sopt.artoo.dto.DisplayContent;

import java.util.List;

@Mapper
public interface DisplayMapper {
    @Select("SELECT * FROM display WHERE ")
    List<Display> findNow(@Param("month") final String nowMonth);

    @Select("SELECT * FROM display WHERE ")
    List<Display> findApp(@Param("month") final String nowMonth);

    @Select("SELECT * FROM artwork, display_content WHERE art_work.a_idx=display_content.a_idx and " +
            "display_content.d_idx=#{displayIdx}")
    List<DisplayContent> findDisplayDetail(@Param("d_idx") final int d_idx);
}
