package org.sopt.artoo.model;

import lombok.Data;

@Data
public class DisplayApplyConfirmRes {
    private int d_idx;
    private String d_title;
    private String d_subTitle;

    private int u_idx;
    private String u_name;

    private int a_idx;
    private String a_name;

    public DisplayApplyConfirmRes(int d_idx, String d_title, String d_subTitle, int u_idx, String u_name, int a_idx, String a_name) {
        this.d_idx = d_idx;
        this.d_title = d_title;
        this.d_subTitle = d_subTitle;
        this.u_idx = u_idx;
        this.u_name = u_name;
        this.a_idx = a_idx;
        this.a_name = a_name;
    }
}
