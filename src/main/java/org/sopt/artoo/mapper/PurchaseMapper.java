package org.sopt.artoo.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.artoo.dto.Purchase;
import org.sopt.artoo.model.PurchaseReq;

import java.util.List;

@Mapper
public interface PurchaseMapper {


    /**
     * 구매 정보 저장
     *
     * @param purchaseReq
     * @return 구매 고유 번호
     */
    @Insert("INSERT INTO purchase(p_state, p_date, p_comment, a_idx, p_seller_idx, p_buyer_idx, p_recipient, p_address, p_phone, p_isCard, p_price) " +
            "VALUES(#{purchaseReq.p_state}, #{purchaseReq.p_currentTime}, #{purchaseReq.p_comment}, #{purchaseReq.a_idx}, #{purchaseReq.p_sellerIdx}, " +
            "#{purchaseReq.p_buyerIdx}, #{purchaseReq.p_recipient}, #{purchaseReq.p_address}, #{purchaseReq.p_phone}, " +
            "#{purchaseReq.p_payment}, #{purchaseReq.p_price})")
    @Options(useGeneratedKeys = true, keyColumn = "purchase.p_idx")
    int savePurchaseData(@Param("purchaseReq") final PurchaseReq purchaseReq);

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

    /**
     * 구매 조회 - p_state 가 결제 완료인 로우만
     *
     * @param p_buyer_idx
     * @return List<Purchase> 구매
     */
    @Select("SELECT * FROM purchase WHERE p_buyer_idx=#{p_buyer_idx} order by p_date DESC")
    List<Purchase> findByBuyerIdx(@Param("p_buyer_idx") final int p_buyer_idx);

    /**
     * 판매 조회 - p_state 가 판매 완료인 로우만
     *
     * @param p_seller_idx
     * @return List<Purchase> 판매
     */
    @Select("SELECT * FROM purchase WHERE p_seller_idx=#{p_seller_idx} order by p_date DESC")
    List<Purchase> findBySellerIdx(@Param("p_seller_idx") final int p_seller_idx);

    /**
     * 미술작품 인덱스로 미완료 거래조회
     */
    @Select("SELECT * FROM purchase WHERE a_idx = #{a_idx} AND ((p_state BETWEEN 11 AND 13) OR (p_state BETWEEN 21 AND 23))")
    List<Purchase> findUncompletedTransactions(@Param("a_idx") final int a_idx);

    /**
     * 미술작품 인덱스로 거래내역 조회
     * @param a_idx
     * @return 거래 내역 리스트
     */
    @Select("SELECT * FROM purchase WHERE a_idx = #{a_idx}")
    List<Purchase> findTransactionsByArtIdx(@Param("a_idx") final int a_idx);

    /**
     *
     * @param p_idx 거래 고유 번호
     * @return Purchase 거래 내역
     */
    @Select("SELECT * FROM purchase WHERE p_idx = #{p_idx}")
    Purchase findPurchaseByPurchaseIdx(@Param("p_idx") final int p_idx);

    /**
     * purchase 인덱스로 purchase 업데이트 하기
     * @param p_idx
     * @param p_comment
     */
    @Update("UPDATE purchase SET p_comment = #{p_comment} WHERE p_idx = #{p_idx}")
    void updatePurchaseComment(@Param("p_idx") final int p_idx, @Param("p_comment") final String p_comment);

    /**
     * purchase 고유 번호로 purchase state 업데이트
     * @param p_idx
     * @param p_state
     */
    @Update("Update purchase SET p_state = #{p_state} WHERE p_idx = #{p_idx}")
    void updatePurchaseState(@Param("p_idx") final int p_idx, @Param("p_state") final int p_state);

    @Delete("DELETE FROM purchase WHERE p_idx = #{p_idx}")
    void deletePurchaseRow(@Param("p_idx") final int p_idx);

}