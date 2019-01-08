package org.sopt.artoo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    // 댓글 고유 인덱스 (pk)
    private int c_idx;
    // 댓글 내용
    private String c_content;
    // 댓글 작성 날짜시간
    private Date c_date;
    // 댓글 작성 idx
    private int u_idx;
    // 작품 idx
    private int a_idx;
    // 권한 체크
    private boolean auth;
    // 댓글 작성자 이름
    private String u_name;
}
