package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.artoo.dto.Home;
import org.sopt.artoo.dto.Tag;

import java.util.List;

@Mapper
public interface HomeMapper {

    //좋아요가 많은 순서대로 5명의 작가 이름
    @Select("SELECT u_name, u_idx FROM (SELECT u_name, u_idx" +
            " FROM artwork, user WHERE artwork.user_u_idx = user.u_idx ORDER BY a_like_count DESC)" +
            "WHERE ROWNUM <=5")
    List<Home> findTodayArtist();

    //작가 정보, 작품 이름, 작품 연도
    @Select("SELECT u_name, a_name, a_year  FROM artwork, user WHERE artwork.u_idx = #{u_idx} AND user.u_idx = #{u_idx}")
    String findTodayContents(@Param("u_idx") final int u_idx);

    //전체 테마 정보
    @Select("SELECT * FROM tag")
    List<Tag> findAllTag();

//    //테마 tag에 맞는 *에 들어있는 a_idx넣어주고 a_idx에 따른 이미지 검색
//    @Select("SELECT a_idx FROM tag WHERE t_idx = #{t_idx}")
//    List<Integer> findTagArtowrkIndexes(@Param("t_idx") final int t_idx);

    //테마 tag에 맞는 *에 들어있는 a_idx넣어주고 a_idx에 따른 이미지 검색
    @Select("SELECT a_idx FROM tag WHERE t_idx = #{t_idx}")
    int findTagArtworkIndex(@Param("t_idx") final int t_idx);




}
