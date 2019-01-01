package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.artoo.dto.ArtworkLike;

import java.util.List;

@Mapper
public interface ArtworkLikeMapper {
    /**
     * 유저별 좋아요 컬럼 반환
     *
     * @param u_idx
     * @return List<ArtworkLike> 좋아요 리스트
     */
    @Select("SELECT * FROM artworkLike WHERE artworkLike.u_idx = #{u_idx}")
    List<ArtworkLike> findArtworkLikeByUserIdx(@Param("u_idx") final int u_idx);
    /**
     *  미술작품별 좋아요 컬럼 반환
     *
     */
    @Select("SELECT * FROM artworkLike WHERE artworkLike.a_idx = #{a_idx}")
    List<ArtworkLike> findArtworkLikeByArtIdx(@Param("a_idx") final int a_idx);

    @Select("SELECT * FROM artworkLike WHERE artworkLike.a_idx = #{a_idx} AND artworkLike.u_idx = #{u_idx}")
    ArtworkLike findByUserIdxAndArtworkIdx(@Param("u_idx") final int u_idx, @Param("a_idx") final int a_idx);

    @Insert("INSERT INTO artworkLike(a_idx, u_idx, al_date) VALUES(#{a_idx},#{u_idx},#{al_date})")
    void save(@Param("u_idx") final int u_idx, @Param("a_idx") final int a_idx, @Param("al_date") final String al_DATETIME);

    @Delete("DELETE FROM artworkLike WHERE artworkLike.u_idx = #{u_idx} AND artworkLike.a_idx = #{a_idx}")
    void deleteByUserIdxAndArtworkIdx(@Param("u_idx") final int u_idx, @Param("a_idx") final int a_idx);
}
