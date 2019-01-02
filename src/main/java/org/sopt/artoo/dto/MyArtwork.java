package org.sopt.artoo.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class MyArtwork {
    private int a_idx;
    // 작품 고유 번호
    private String a_url;
    // 그림 url
    private boolean a_isDisplay;
    // 전시 확인
}
