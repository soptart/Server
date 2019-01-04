package org.sopt.artoo.dto;

import lombok.Data;


@Data
public class UserPurchase {
    //MyPageRes 거래내역
    private int p_idx;
    // 구매 인덱스
    private int a_idx;
    // 작품 인덱스
    private String a_name;
    // 작품 이름
    private boolean isBuyer;
    // 판매자 or 구매자 확인
    private String u_name;
    // 판매자 or 구매자 이름
    private int a_price;
    // 작품 가격
    private int p_state;
    // 거래 상태
    private String a_pic_url;
    // 작품 그림 URL
    private String p_date;
    // 거래 시간
}
