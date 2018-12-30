package org.sopt.artoo.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.artoo.dto.ArtworkPic;

import java.util.List;


@Mapper
public interface ArtworkPicMapper {

    /**
     * 작품 사진 조회
     * @param a_idx
     * @return 사진 한 장
     */
    @Select("SELECT * FROM artworkPic WHERE a_idx = #{a_idx}")
    ArtworkPic findByArtIdx(@Param("a_idx") final int a_idx);

    @Insert("INSERT INTO artworkPic(a_idx, pic_url) VALUES(#{a_idx}, #{pic_url})")
    void save(@Param("a_idx") final int a_idx, @Param("pic_url") final String pic_url);


    /**
     * 작품 사진 조회
     * @param a_idx
     * @return 사진 여러장
     */
    @Select("SELECT * FROM artworkPic WHERE a_idx = #{a_idx}")
    List<ArtworkPic> findPicListByArtIdx(@Param("a_idx") final int a_idx);

//    @Select("SELECT pic_url FROM artworkPic, artwork WHERE artwork.u_idx = #{u_idx}")
//    List<ArtworkPic> findPicListByUserIdx(@Param("u_idx") final int u_idx);

    /**
     * 태그 추천 작품 사진 조회
     * @param a_idx
     * @return 사진 6개
     */
    @Select("SELECT pic_url FROM artworkPic WHERE a_idx = #{a_idx} LIMIT 6")
    List<ArtworkPic> findRecPicListByArtIdx(@Param("a_idx") final int a_idx);

}
