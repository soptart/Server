package org.sopt.artoo.dto;

import lombok.Data;
import org.sopt.artoo.model.DefaultRes;

import java.util.Date;

@Data
public class Artwork {
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
    private String a_category;
    // 작품 형태
    private String a_form;
    // 작품 가격
    private int a_price;
    // 작품 좋아요 수
    private int a_like_count;
    // 유저 고유 인덱스
    private int u_idx;
    // 작품 설명
    private String a_detail;
    // 작품 작성날
    private Date a_date;
    // 작품 제작년도
    private String a_year;
    // 작품 사진
    private String pic_url;
    // 작품 재료
    private String a_material;
    // 작품 표현 기법
    private String a_expression;
    // 직거래-판매 비판매
    private int a_purchaseState; // 0-비구매 1-구매 2-판매완료

    //수정삭제권한
    private boolean auth;
    // 좋아요 여부
    private boolean islike;
    // 작품 테그
    private String a_tags;
    // 작품 라이센스
    private String a_license;
    // 작품 사이즈
    private int a_size;
    // 전시 유무
    private boolean a_isDisplay;
    // 작품 활성화/비활성화
    private boolean a_active;
}