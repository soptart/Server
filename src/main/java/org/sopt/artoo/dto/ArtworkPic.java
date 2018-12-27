package org.sopt.artoo.dto;

import lombok.Data;

@Data
public class ArtworkPic {
    // 사진 고유 번호
    private int ap_idx;
    // 미술작품 고유 번호
    private int a_idx;
    // 사진 주소
    private String pic_url;
}
