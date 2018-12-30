package org.sopt.artoo.model;

import lombok.Data;

import java.util.Date;

@Data
public class CommentReq {
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
    // 권한
    private boolean auth;

    public boolean checkProperties(){
        return c_content != null && u_idx > 0 && a_idx > 0;
        //return (c_comment!=null&&u_idx>0&&a_idx>0);
    }
}
