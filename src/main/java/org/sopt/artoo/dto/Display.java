package org.sopt.artoo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Slf4j
@Data
@AllArgsConstructor
public class Display {
    // 고유 idx
    private int d_idx;
    // 전시 date
    private String d_sDateNow;
    private String d_eDateNow;

    // 신청 date
    private String d_sDateApply;
    private String d_eDateApply;

    // 전시 대문사진 url
    private String d_repImg_url;
    private String d_titleImg_url;
    private String d_mainImg_url;

    // 전시 title
    private String d_title;
    private String d_subTitle;

    //전시 설명
    private String d_longDetail;
    private String d_shortDetail;
    private List<String> d_artworkUser;
}
