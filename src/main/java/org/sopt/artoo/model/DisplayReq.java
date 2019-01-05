package org.sopt.artoo.model;

import lombok.Data;

@Data
public class DisplayReq {
    // 작품 고유 인덱스
    private int a_idx =-1;
    // 작가 고유 인덱스
    private int u_idx=-1;
    // 전시회 고유 인덱스
    private int d_idx=-1;
    // 전시 등록 시간
    private String dc_date;

    public boolean checkProperties() {
        return (a_idx >=0 && u_idx>=0 && d_idx >=0);
    }
}