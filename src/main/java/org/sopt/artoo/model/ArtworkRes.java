package org.sopt.artoo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.dto.User;

import java.util.Date;

@Getter
@Setter
public class ArtworkRes {

    public ArtworkRes(Artwork artwork, User user) {
        this.a_idx = artwork.getA_idx();
        this.a_name = artwork.getA_name();
        this.a_width = artwork.getA_width();
        this.a_height = artwork.getA_height();
        this.a_depth = artwork.getA_depth();
        this.a_price = artwork.getA_price();
        this.a_like_count = artwork.getA_like_count();
        this.a_detail = artwork.getA_detail();
        this.a_date = artwork.getA_date();
        this.a_year = artwork.getA_year();
        this.pic_url = artwork.getPic_url();
        this.a_material = artwork.getA_material();
        this.a_expression = artwork.getA_expression();
        this.a_license = artwork.getA_license();
        this.a_purchaseState = artwork.getA_purchaseState();

        this.u_idx = user.getU_idx();
        this.u_name = user.getU_name();
        this.u_school = user.getU_school();
    }

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

    // 작품 가격
    private int a_price;
    // 작품 좋아요 수
    private int a_like_count;
    // 작품 설명
    private String a_detail;

    // 작품 작성날
    private Date a_date;
    // 작품 제작년도
    private String a_year;
    // 작품 사진
    private String pic_url;

    private String a_material;
    private String a_expression;
    private int a_purchaseState;

    //유저 정보
    private int u_idx;
    private String u_name;
    private String u_school;

    //수정삭제권한
    private boolean auth;

    // 좋아요 여부
    private boolean islike;

    // 작품 라이센스
    private String a_license;
    // 작품 사이즈
    private int a_size;

    // 작품 활성화/비활성화
    private boolean a_active;
}