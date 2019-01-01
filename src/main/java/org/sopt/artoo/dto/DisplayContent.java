package org.sopt.artoo.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DisplayContent {
    // displayContent 고유 idx
    private int dc_idx;
    // artwork idx
    private int a_idx;
    // artwork idx
    private int u_idx;
    // artwork idx
    private int d_idx;
}