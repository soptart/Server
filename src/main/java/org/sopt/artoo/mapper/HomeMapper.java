package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.artoo.dto.Home;

import java.util.List;

@Mapper
public interface HomeMapper {

    //좋아요가 많은 순서대로 5명의 작가 이름
    @Select("SELECT u_name, u_idx FROM (SELECT u_name, u_idx" +
            " FROM artwork, user WHERE artwork.user_u_idx = user.u_idx ORDER BY a_like_count DESC)" +
            "WHERE ROWNUM <=5")
    List<Home> findTodayArtist();

    //작가의 작품 리스트
    @Select("SELECT u_name, a_thumb_url, a_name, a_year  FROM artwork, user WHERE artwork.u_idx = #{u_idx} and user.u_idx = #{u_idx}")
    List<List<Home>> findTodayContents(@Param("u_idx") final int u_idx);
}
