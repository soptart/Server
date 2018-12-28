package org.sopt.artoo.model;

import lombok.Data;

@Data
public class DisplayReq {
    // 작품 고유 인덱스
    private int a_idx;
    // 작가 고유 인덱스
    private int u_idx;
    // 전시회 고유 인덱스
    private int d_idx;
}