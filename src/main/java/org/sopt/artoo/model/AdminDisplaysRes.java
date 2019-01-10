package org.sopt.artoo.model;


import lombok.Getter;
import lombok.Setter;
import org.sopt.artoo.dto.Display;


@Getter
@Setter
public class AdminDisplaysRes {
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

    // 전시 신청한 작품 개수
    private int d_count ;
    private int d_state;

    public AdminDisplaysRes(Display display) {
        this.d_idx = display.getD_idx();
        this.d_sDateNow = display.getD_sDateNow();
        this.d_eDateNow = display.getD_eDateNow();
        this.d_sDateApply = display.getD_sDateApply();
        this.d_eDateApply = display.getD_eDateApply() ;
        this.d_repImg_url = display.getD_repImg_url();
        this.d_titleImg_url = display.getD_titleImg_url();
        this.d_mainImg_url = display.getD_mainImg_url();
        this.d_title = display.getD_title();
        this.d_subTitle = display.getD_subTitle();
        this.d_longDetail = display.getD_longDetail();
        this.d_shortDetail = display.getD_shortDetail();
    }
}
