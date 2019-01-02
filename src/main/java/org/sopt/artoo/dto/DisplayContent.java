package org.sopt.artoo.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Data
public class DisplayContent {
    // displayContent 고유 idx
    private int dc_idx;
    // artwork idx
    private int a_idx;
    // user idx
    private int u_idx;
    // display idx
    private int d_idx;
    // 전시 신청 날짜
    private Date dc_date;
}