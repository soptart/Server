package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.artoo.dto.Home;
import org.sopt.artoo.dto.Tag;

import java.util.List;

@Mapper
public interface HomeMapper {

    //좋아요가 많은 순서대로 5명의 작가 index
    @Select("SELECT u_idx FROM artworkLike GROUP BY u_idx ORDER BY COUNT(*) DESC LIMIT 5")
    List<Integer> findUserIdx();


    /**
     * 작가 이름, 작품명, 제작년도, artwork index 정보
     * @param u_idx
     * @return 작가 이름, 작품명, 제작년도, artwork index 정보
     */
    @Select("SELECT u_name, a_name, a_year, a_idx  FROM artwork, user WHERE artwork.u_idx = #{u_idx} AND user.u_idx = #{u_idx}")
    Home findTodayContents(@Param("u_idx") final int u_idx);


    //전체 테마 정보
    @Select("SELECT * FROM tag")
    List<Tag> findAllTag();

    /**
     * artwork index 정보
     * @param t_idx
     * @return a_idx
     */
    @Select("SELECT a_idx FROM artwork WHERE artwork.t_idx = {t_idx}")
    int findArtworkIdxByTagIdx(@Param("t_idx") final int t_idx);


    /**
     * artwork index
     * @return tag 정보
     */




}
