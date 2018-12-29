package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.artoo.dto.Display;
import org.sopt.artoo.dto.DisplayContent;

import java.util.List;

@Mapper
public interface DisplayMapper {
    @Select("SELECT * FROM display")
    List<Display> findAllDisplay();

    @Select("SELECT * FROM display WHERE d_idx=#{d_idx}")
    Display findByDisplayidx(@Param("d_idx") final int d_idx);
}