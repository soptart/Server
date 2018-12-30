package org.sopt.artoo.dto;

import lombok.Data;

import java.util.List;

@Data
public class Home {
    //작가 고유 번호
    int u_idx;
    //작가 이름
    String u_name;
    //작가 소개
    String u_description;
    //사진 정보
    List<HomeData> pic_Info;
}