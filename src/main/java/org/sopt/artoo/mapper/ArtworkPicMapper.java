package org.sopt.artoo.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.artoo.dto.ArtworkPic;

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

}
