package org.sopt.artoo.dto;

import lombok.Data;

import java.util.List;

@Data
public class Home {
    //작가 고유 번호
    int u_idx;
    //artworks 고유 변호
    int a_idx;
    //작가 이름
    String u_name;
    //작품 이름
    String a_name;
    //대표 사진
    String a_thumb_url;
    //좋아요
    int a_like_count;

}
