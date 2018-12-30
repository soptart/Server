package org.sopt.artoo.dto;

import java.util.Date;

public class Comment {
    // 댓글 고유 인덱스 (pk)
    private int c_idx;
    // 댓글 내용
    private String c_comment;
    // 댓글 작성 날짜시간
    private Date c_date;
    // 댓글 작성 idx
    private int u_idx;
    // 작품 idx
    private int a_idx;
}
