package org.sopt.artoo.model;

import lombok.Data;

import java.util.Date;

@Data
public class ArtworkReq {

    // 작품 고유 인덱스
    private int a_idx;
    // 작품 이름
    private String a_name;
    // 작품 가로
    private int a_width;
    // 작품 세로
    private int a_height;
    // 작품 높이
    private int a_depth;
    // 작품 카테고리
    private int a_category;
    // 작품 형태
    private int a_form;
    // 작품 가격
    private int a_price;
    // 작품 좋아요 수
    private int a_like_count;
    // 유저 고유 인덱스
    private int u_idx;
    // 댓글 고유 인덱스
    private int c_idx;
    // 작품 설명
    private String a_detail;
    // 작품 작성날
    private Date a_date;
    // 작품 제작년도
    private String a_year;

    public boolean checkProperties() {

    }
}
