package org.sopt.artoo.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.dto.Purchase;
import org.sopt.artoo.dto.User;

import java.util.Date;

@Slf4j
@Data
public class NoticeRes {
    // 구매 인덱스
    private int p_idx;
    // 구매 상태
    private int p_state; //결제완료인 거래내역만 보여줌... from p_state
    // 배송 방식
    private int p_delivery; // from p_state  0-택배 1-직거래
    // 구매 날짜 (12.03(목))
    private String p_date;

    // 작품 인덱스 (FK)
    private int a_idx;
    // 작품명
    private String a_name;
    // 작가
    private String a_u_name;

    // (판매자 혹은 구매자) u_idx
    private int u_idx;
    // (판매자 혹은 구매자) 이름
    private String u_name;
    // (판매자 혹은 구매자) 핸드폰 번호
    private String u_phone;
    // (판매자 혹은 구매자) 주소
    private String u_address;

    public NoticeRes(Purchase purchase) throws Exception{
        this.p_idx = purchase.getP_idx();
        this.p_state = purchase.getP_state();
//        this.p_delivery = purchase;
//        log.info(purchase.getP_date().toString());

//        this.p_date = DateRes.getDate1(purchase.getP_date().toString());
        this.a_idx = purchase.getA_idx();
    }
}