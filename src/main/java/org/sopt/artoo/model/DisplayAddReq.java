package org.sopt.artoo.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DisplayAddReq {

    // 작품 고유 인덱스
    private int d_idx;
    // 전시 date
    private String d_sDateNow;
    private String d_eDateNow;

    // 신청 date
    private String d_sDateApply;
    private String d_eDateApply;

    // 전시 대문사진 url
    private MultipartFile m_d_repImg_url;
    private MultipartFile m_d_titleImg_url;
    private MultipartFile m_d_mainImg_url;

    private String d_repImg_url;
    private String d_titleImg_url;
    private String d_mainImg_url;

    // 전시 title
    private String d_title;
    private String d_subTitle;

    //전시 설명
    private String d_longDetail;
    private String d_shortDetail;

//    public boolean checkProperties() {
//        return ( !d_repImg_url.equals("") && !d_title.equals("") && !d_subTitle.equals(""));
//    }

    public boolean checkProperties() {
        return ( m_d_repImg_url!=null && d_title != null && d_subTitle != null);
    }

}
