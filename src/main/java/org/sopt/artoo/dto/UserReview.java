package org.sopt.artoo.dto;

import lombok.Data;

@Data
public class UserReview {
    private int p_idx;
    // 구매 고유 번호
    private String a_name;
    // 작품 이름
    private String u_name;
    // 구매자 이름
    private String p_comment;
    // 구매자 리뷰
    private String p_date;
    // 구매일
    private String a_pic_url;
    // 작품 사진 Url
}
