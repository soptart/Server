package org.sopt.artoo.dto;

import lombok.Data;

@Data
public class Purchase {
    private int p_idx;
    // 구매 인덱스
    private int p_state;
    // 구매 상태
    private boolean p_isBuyer;
    // 구매자 / 판매자 구분
    private String p_date;
    // 구매 날짜
    private String p_comment;
    // 구매 댓글
    private int a_idx;
    // 작품 인덱스 (FK)
    private int u_idx;
    // 구매 유저 인덱스 (FK)

    public Purchase(){
        p_isBuyer = false;
    }
}
