package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.artoo.dto.Purchase;

import java.util.List;

@Mapper
public interface PurchaseMapper {

    /**
     * 특정 회원의 구매 내역 반환
     *
     * @param u_idx
     * @return List<Purchase> 판매 + 구매
     */
    @Select("SELECT * FROM purchase WHERE purchase.u_idx = #{u_idx} UNION " +
            "SELECT * FROM purchase WHERE a_idx IN (SELECT a_idx FROM artwork WHERE artwork.u_idx = #{u_idx})")
    List<Purchase> findTransactionByUserIdx(@Param("u_idx") final int u_idx);


}
