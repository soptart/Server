package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.artoo.dto.Purchase;
import org.sopt.artoo.model.PurchaseReq;

import java.util.List;

@Mapper
public interface PurchaseMapper {

    @Insert("INSERT INTO purchase(p_idx, p_state, p_date, p_comment, a_idx, p_buyer_idx, p_seller_idx, p_recipient, p_address) " +
            "VALUES(#{purchaseReq.p_idx},#{purchaseReq.p_state},#{purchaseReq.p_date},#{purchaseReq.p_comment},#{purchaseReq.a_idx},#{purchaseReq.p_buyer_idx},#{purchaseReq.p_seller_idx},#{purchaseReq.p_recipient},#{purchaseReq.p_address})")
    void savePurchase(final PurchaseReq purchaseReq);

    /**
     * 특정 회원의 구매 내역 반환
     *
     * @param u_idx
     * @return List<Purchase> 판매 + 구매
     */
    @Select("(SELECT * FROM purchase WHERE purchase.p_buyer_idx = #{u_idx}) UNION "+
            "(SELECT * FROM purchase WHERE purchase.p_seller_idx = #{u_idx})" +
            "ORDER BY p_date DESC")
    List<Purchase> findTransactionByUserIdx(@Param("u_idx") final int u_idx);


}
