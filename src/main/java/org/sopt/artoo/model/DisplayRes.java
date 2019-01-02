package org.sopt.artoo.model;

import lombok.Data;
import org.sopt.artoo.dto.Display;

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

    public DisplayRes(Display display, int a_idx, String a_name, int u_idx, String u_name) {
        this.display = display;
        this.a_idx = a_idx;
        this.a_name = a_name;
        this.u_idx = u_idx;
        this.u_name = u_name;
    }
}

