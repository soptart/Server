package org.sopt.artoo.model;

import lombok.Data;

@Data
public class UserSignUpReq {
    public final static int original = 0;
    public final static int Kakao = 1;
    public final static int Naver = 2;
    //회원 가입시 입력

    private String u_email;
    // 유저 ID(이메일)
    private String u_pw;
    // 유저 비밀번호
    private String u_school;
    // 유저 학교
    private String u_phone;
    // 유저 핸드폰 번호
    private String u_name;
    // 유저 이름
    private String u_description;
    // 유저 정보 변경
    private String accessToken;
    // 카카오 rest api key
    // 유저 로그인 타입
    private int u_type;
    // 외부 인덱스
    private int external_key;

    public boolean checkQualification() {
        return (u_email != null && u_pw != null && u_phone != null && u_name != null);
    }
}
