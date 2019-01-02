package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.artoo.dto.Home;
import org.sopt.artoo.dto.HomeData;
import org.sopt.artoo.dto.Tag;

import java.util.List;

@Mapper
public interface HomeMapper {

    /**
     * 좋아요가 많은 5명의 작가 순서대로
     */
    @Select("SELECT  a.u_idx, sum(C) AS likeAmount FROM (SELECT artworkLike.a_idx, artwork.u_idx, COUNT(artworkLike.a_idx) AS C \n" +
            "FROM artworkLike, artwork WHERE artwork.a_idx = artworkLike.a_idx AND artwork.a_active = 1 GROUP BY artworkLike.a_idx) a " +
            "GROUP BY a.u_idx ORDER BY sum(C) DESC LIMIT 5")
    List<Integer> findTodayUserIdx();


    /**
     * 작가 이름, 작품명, 제작년도, artwork index 정보
     * @param u_idx
     * @return 작품명, 제작년도, artwork index 정보
     */
    @Select("SELECT a_name, a_year, a_idx  FROM artwork, user WHERE artwork.u_idx = #{u_idx} AND user.u_idx = #{u_idx}")
    List<HomeData> findArtistContentsByUserIdx(@Param("u_idx") final int u_idx);


    /**
     * 작가 이름, 작가 소개
     * @param u_idx
     * @return Home
     */
    @Select("SELECT DISTINCT u_name, u_description FROM artwork, user WHERE artwork.u_idx = #{u_idx} AND user.u_idx = #{u_idx}")
    Home findArtistNameDescriptByUserIdx(@Param("u_idx") final int u_idx);


    /**
     * 테마 전체 정보
     * @return List<Tag>
     */
    @Select("SELECT * FROM tag")
    List<Tag> findAllTag();


}
