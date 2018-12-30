package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.artoo.dto.ArtworkLike;

import java.util.List;

@Mapper
public interface LikeMapper {
    /**
     * 유저별 좋아요 컬럼 반환
     *
     * @param u_idx
     * @return List<ArtworkLike> 좋아요 리스트
     */
    @Select("SELECT * FROM artworkLike WHERE artworkLike.u_idx = #{u_idx}")
    List<ArtworkLike> findArtworkLikeByUserIdx(@Param("u_idx") final int u_idx);
}
