package org.sopt.artoo.dto;

import lombok.Data;

import java.util.List;

@Data
public class Home {
    //작가 고유 번호
    private int u_idx;
    //작가 이름
    private String u_name;
    //작가 소개
    private String u_description;
    //사진 정보
    private List<HomeData> list;
}