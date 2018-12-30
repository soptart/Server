package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.model.ArtworkReq;

import java.util.List;

@Mapper
public interface ArtworkMapper {

    /**
     * 미술작품 전체 조회
     *
     * @return 미술작품전체
     */
    @Select("SELECT * FROM artwork")
    List<Artwork> findAll();

    /**
     * 미술작품 인덱스로 조회
     *
     * @param a_idx
     * @return 미술작품객체
     */
    @Select("SELECT * FROM artwork WHERE a_idx = #{a_idx}")
    Artwork findByIdx(@Param("a_idx") final int a_idx);

    /**
     * 특정 작가별 작품 조회
     *
     * @param u_idx
     * @return 미술작품객체 리스트
     */
    @Select("SELECT * FROM artwork WHERE u_idx = #{u_idx}")
    List<Artwork> findArtworkByUserIdx(@Param("u_idx") final int u_idx);


    /**
     * 미술작품 저장
     *
     * @param artworkReq
     */


    @Insert("INSERT INTO artwork(a_idx, a_name, a_width, a_height, a_depth, a_category, a_form, a_price, a_like_count, u_idx, a_detail, a_date, a_year,a_tags,a_license) " +
            "VALUES(#{artworkReq.a_idx}, #{artworkReq.a_name}, #{artworkReq.a_width},#{artworkReq.a_height},#{artworkReq.a_depth},#{artworkReq.a_category},#{artworkReq.a_form},#{artworkReq.a_price},#{artworkReq.a_like_count},#{artworkReq.u_idx}, #{artworkReq.a_detail},#{artworkReq.a_date}, #{artworkReq.a_year},#{artworkReq.a_tags}, #{artworkReq.a_license})")
    @Options(useGeneratedKeys = true, keyProperty = "artworkReq.a_idx")
    void save(@Param("artworkReq") final ArtworkReq artworkReq);

    @Update("UPDATE artwork SET a_name = #{artworkReq.a_name}, a_width = #{artworkReq.a_width}, a_height = #{artworkReq.a_height}, a_depth = #{artworkReq.a_depth}, a_category = #{artworkReq.a_category}, a_form = #{artworkReq.a_form}, a_price = #{artworkReq.a_price}, a_like_count = #{artworkReq.a_like_count}, a_detail = #{artworkReq.a_detail}, a_date = #{artworkReq.a_date}, a_year = #{artworkReq.a_year} WHERE a_idx = #{a_idx}")
    void updateByArtIdx(@Param("artworkReq") final ArtworkReq artworkReq, @Param("a_idx") final int a_idx);

    @Delete("DELETE FROM artwork WHERE a_idx = #{a_idx}")
    void deleteByArtIdx(@Param("a_idx") final int a_idx);


    /**
     * 미술작품 태그, a_idx
     *
     * @return a_tags, a_idx
     */
    @Select("SELECT a_tags, a_idx FROM artwork")
    List<Artwork> findTagsArtworkIdx();

}
