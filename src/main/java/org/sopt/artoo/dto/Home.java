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
//
//    public Home(int u_idx, String u_name, String u_description, List<HomeData> pic_Info) {
//        this.u_idx = u_idx;
//        this.u_name = u_name;
//        this.u_description = u_description;
//        this.pic_Info = pic_Info;
//    }


}
