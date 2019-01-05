package org.sopt.artoo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.dto.Purchase;
import org.sopt.artoo.dto.User;

import java.util.Date;

@Slf4j
@Getter
@Setter
public class NoticeRes {
    // 구매 인덱스
    private int p_idx;
    // 구매 상태
    private int p_state; // 결제완료인 거래내역만 보여줌... from p_state 결제전(0). 직거래(1), 택배(2)// 판매완료-직거래(3) 판매완료-택배(4)
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
    // (판매자 혹은 구매자) 은행
    private String u_bank;
    // (판매자 혹은 구매자) 계좌번호
    private String u_account;

    public NoticeRes(Purchase purchase) throws Exception{
        this.p_idx = purchase.getP_idx();
        this.p_state = purchase.getP_state();
        this.a_idx = purchase.getA_idx();
    }
}