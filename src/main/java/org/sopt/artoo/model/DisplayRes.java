package org.sopt.artoo.model;

import lombok.Data;
import org.sopt.artoo.dto.Display;

import java.util.Date;

@Data
public class DisplayRes {
    //전시회 정보
    Display display;

    //작품 정보
    private int a_idx;
    private String a_name;

    //유저 정보
    private int u_idx;
    private String u_name;

    private int state; //1- 전시신청완료 2- 전시완료 3- 확정되어서 대기 중인 전시

    private String dc_date;

//    public DisplayRes(Display display, int a_idx, String a_name, int u_idx, String u_name, int state, String dc_date) {
//        this.display = display;
//        this.a_idx = a_idx;
//        this.a_name = a_name;
//        this.u_idx = u_idx;
//        this.u_name = u_name;
//        this.state = state;
//    }
}

