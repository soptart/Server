package org.sopt.artoo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DisplayApply {
    private int d_idx;
    private String d_title;

    private Date d_sDateApply;
    private Date d_eDateApply;
    private Date d_sDateNow;
    private Date d_eDateNow;

    private String[] a_thumb_url;
}