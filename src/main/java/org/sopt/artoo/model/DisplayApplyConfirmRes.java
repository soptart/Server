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
    private String dc_date;

}
