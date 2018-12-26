package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.model.ArtworkReq;

@Mapper
public interface ArtworkMapper {

    /**
     * 미술작품 전체 조회
     *
     * @return 미술작품전체
     */
    @Select("SELECT * FROM artwork")
    Artwork findAll();

    /**
     * 미술작품 인덱스로 조회
     *
     * @param a_idx
     * @return 미술작품객체
     */
    @Select("SELECT * FROM artwork WHERE a_idx = #{a_idx}")
    Artwork findByIdx(@Param("a_idx") final int a_idx);

    /**
     * 미술작품 저장
     *
     * @param artworkReq
     */
    @Insert("INSERT INTO artwork(a_idx, user_u_idx, comments_c_idx, a_name, a_width, a_height, a_depth, a_category, a_form, a_price, a_thumb_url, a_like_count, a_detail) " +
            "VALUES(#{artworkReq.a_idx, artworkReq.user_u_idx, artworkReq.comments_c_idx,artworkReq.a_name,artworkReq.a_width,artworkReq.a_height,artworkReq.a_depth,artworkReq.a_category,artworkReq.a_form,artworkReq.a_price,artworkReq.a_thumb_url,artworkReq.a_like_count,artworkReq.a_detail})")
    @Options(useGeneratedKeys = true, keyProperty = "artworkReq.a_idx")
    void save(@Param("artworkReq") final ArtworkReq artworkReq);

    @Update("UPDATE artwork SET a_name = #{artworkReq.a_name}, a_width = #{artworkReq.a_width}, a_height = #{artworkReq.a_height}, a_depth = #{artworkReq.a_depth}, a_category = #{artworkReq.a_category}, a_form = #{artworkReq.a_form}, a_price = #{artworkReq.a_price}, a_thumb_url = #{artworkReq.a_thumb_url}, a_like_count = #{artworkReq.a_like_count}, a_detail = #{artworkReq.a_detail} WHERE a_idx = #{a_idx}")
    void updateByArtIdx(@Param("artworkReq") final ArtworkReq artworkReq, @Param("a_idx") final int a_idx);

    @Delete("DELETE FROM artwork WHERE a_idx = #{a_idx}")
    void deleteByArtIdx(@Param("a_idx") final int a_idx);
}
