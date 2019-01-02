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
    @Select("SELECT * FROM artwork ORDER BY artwork.a_date DESC")
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
     * 좋아요 수
     */
    @Update("UPDATE artwork SET a_like_count = #{a_like_count} WHERE a_idx = #{a_idx}")
    void like(@Param("a_idx") final int a_idx, @Param("a_like_count") final int a_like_count);


    /**
     * 미술작품 태그, a_idx
     *
     * @return a_tags, a_idx
     */
    @Select("SELECT a_tags, a_idx FROM artwork")
    List<Artwork> findTagsArtworkIdx();


    /**
     * 미술작품 크기 필터
     * @param max_size
     * @param min_size
     * @return a_idx
     * min_size, max_size 클라에서 'S', 'M' 인지 받아서 S면 min_size = 0, max_size = 2411 설정해서 a_idx받아오기,
     * ArtworkPicMapper findByArtIdx 함수 이용해서 art_pic 받아오기
     */
    @Select("SELECT a_idx FROM artwork WHERE a_size BETWEEN #{min_size} AND #{max_size}")
    List<Integer> findArtIdxBySize(@Param("min_size") final int min_size, @Param("max_size") final int max_size);

    /**
     * 미술 작품 형태 필터(드로잉, 페인팅, 동양화, 혼합 매체, 조형/공예, 사진)
     * @param a_form
     * @return a_idx
     */
    @Select("SELECT a_idx FROM artwork WHERE a_form = #{a_form}")
    List<Integer> findArtIdxByForm(@Param("a_form") final String a_form);

    /**
     * 미술 작품 카테고리(인물, 동물, 식물, 사물, 추상, 풍경)
     */
    @Select("SELECT a_idx FROM artwork WHERE a_category = #{a_category}")
    List<Integer> findArtIdxByCategory(@Param("a_category") final String a_category);




}
