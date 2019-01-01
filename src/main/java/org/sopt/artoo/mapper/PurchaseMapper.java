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
    @Select("(SELECT * FROM purchase WHERE purchase.u_idx = #{u_idx}) UNION " +
            "(SELECT * FROM purchase WHERE a_idx IN (SELECT a_idx FROM artwork WHERE artwork.u_idx = #{u_idx}))" +
            "ORDER BY p_date DESC")
    List<Purchase> findTransactionByUserIdx(@Param("u_idx") final int u_idx);

    /**
     * 구매 조회 - p_state 가 결제 완료인 로우만
     *
     * @param p_buyer_idx
     * @return List<Purchase> 구매
     */
    @Select("SELECT * FROM purchase WHERE p_buyer_idx=#{p_buyer_idx}")
    List<Purchase> findByBuyerIdx(@Param("p_buyer_idx") final int p_buyer_idx);

    /**
     * 판매 조회 - p_state 가 판매 완료인 로우만
     *
     * @param p_buyer_idx
     * @return List<Purchase> 판매
     */
    @Select("SELECT * FROM purchase WHERE p_seller_idx=#{p_seller_idx}")
    List<Purchase> findBySellerIdx(@Param("p_buyer_idx") final int p_buyer_idx);

}
