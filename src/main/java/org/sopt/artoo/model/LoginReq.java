package org.sopt.artoo.model;

import lombok.Data;

@Data
public class LoginReq {
    private String u_email;
    //로그인 이메일
    private String u_pw;
    //로그인 패스워드
}
