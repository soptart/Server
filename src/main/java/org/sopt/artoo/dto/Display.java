package org.sopt.artoo.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Data
public class Display {
    // 고유 idx
    private int d_idx;
    // 전시 date
    private String d_sdateNow;
    private String d_edateNow;

    // 신청 date
    private String d_sdateApply;
    private String d_edateApply;

    // 전시 대문사진 url
    private String d_repimg_url;
    private String d_titleImg_url;
    private String d_mainImg_url;

    // 전시 title
    private String d_title;
    private String subTitle;

    //전시 설명
    private String d_longDetail;
    private String d_shortDetail;

    //진행중 전시 - 1, 신청중 전시 - 0
    private String isNow;
}
