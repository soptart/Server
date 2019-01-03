package org.sopt.artoo.mapper;


import org.apache.ibatis.annotations.*;
import org.sopt.artoo.dto.ArtworkPic;

import java.util.List;


@Mapper
public interface ArtworkPicMapper {

    /**
     * 작품 전체 사진 조회
    * @return 사진 한 장
     */
    @Select("SELECT * FROM artworkPic")
    List<ArtworkPic> findAllArtworkPic();

    /**
     * 작품 사진 조회
     * @param a_idx
     * @return 사진 한 장
     */
    @Select("SELECT * FROM artworkPic WHERE a_idx = #{a_idx}")
    ArtworkPic findByArtIdx(@Param("a_idx") final int a_idx);

    @Insert("INSERT INTO artworkPic(a_idx, pic_url) VALUES(#{a_idx}, #{pic_url})")
    void save(@Param("a_idx") final int a_idx, @Param("pic_url") final String pic_url);

    @Update("UPDATE artworkPic SET pic_url = #{pic_url} WHERE a_idx = #{a_idx}")
    void update(@Param("a_idx") final int a_idx, @Param("pic_url") final String pic_url);

    @Delete("DELETE FROM artworkPic WHERE a_idx = #{a_idx}")
    void deleteByArtIdx(@Param("a_idx") final int a_idx);

}
