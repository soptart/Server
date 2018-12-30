package org.sopt.artoo.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeData {
    /**
     * a_idx 작품 고유 번호
     * a_name 작품 명
     * a_year 제작 연도
     * pic_url 사진 리스트
     */
    private int a_idx;
    private String a_name;
    private String a_year;
    private ArtworkPic pic_url;


}
