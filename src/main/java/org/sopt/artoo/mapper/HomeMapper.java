package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.artoo.dto.Home;

import java.util.List;

public interface HomeMapper {

    /**
     * 해당 컨텐츠의 모든 댓글 조회
     *
     */
    @Select("SELECT * FROM (SELECT * FROM artwork ORDER BY a_like_count DESC)" +
            "WHERE ROWNUM <=5")
    List<Home> findTodayArtist();
}
