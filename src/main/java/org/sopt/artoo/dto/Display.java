package org.sopt.artoo.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Data
public class Display {
    // 고유 idx
    private int d_idx;
    // 전시 시작 date
    private String d_sdate;
    // 전시 마지막 date
    private String d_edate;
    // 전시 대문사진 url
    private String d_imgUrl;
    // 전시 title
    private String d_title;
    //전시 설명
    private String d_detail;


    //진행중 전시 - 1, 신청중 전시 - 0
    private int isNow;
}
