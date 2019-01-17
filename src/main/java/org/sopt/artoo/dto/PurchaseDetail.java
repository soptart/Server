package org.sopt.artoo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PurchaseDetail {
    // 구매 인덱스
    private int p_idx;
    // 구매 상태
    private int p_state;
    // 구매 날짜
    private Date p_date;
    // 구매 후기
    private String p_comment = "";
    // 작품 고유 인덱스
    private int a_idx;
    // 작품 이름
    private String a_name;
    // 작품 구매자 이름
    private String p_buyer_name;
    // 작품 구매자 전화번호
    private String p_buyer_phone;
    // 작품 구매자 계좌번호
    private String p_buyer_account;
    // 작품 판매자 이름
    private String p_seller_name;
    // 작품 판매자 전화번호
    private String p_seller_phone;
    // 작품 판매자 계좌번호
    private String p_seller_account;
    // 작품 수령자 이름
    private String p_recipient;
    // 작품 수령 주소
    private String p_address;
    // 1.7 월추가 p_price(a_price, VAT, (택배))
    private int p_price;
}
