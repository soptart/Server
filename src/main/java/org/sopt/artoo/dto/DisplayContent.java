package org.sopt.artoo.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DisplayContent {
    //전시회 정보
    private int d_idx;
    private String d_title;

    //작품 정보
    private int a_idx;
    private String a_name;
    private int a_width;
    private int a_height;
    private int a_depth;
    private String a_form;
    private String a_year;

    //작품 사진
    private String pic_url;

    //유저 정보
    //?영어이름
    private int u_idx;
    private String u_name;
}