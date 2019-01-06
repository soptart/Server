package org.sopt.artoo.dto;

import lombok.Data;

@Data
public class User {
    //서버에서 필요한 데이터
    private int u_idx;
    // 유저 인덱스
    private String u_email;
    // 유저 ID(이메일)
    private String u_school;
    // 유저 학교
    private String u_phone;
    // 유저 핸드폰 번호
    private int u_credit;
    // 유저 마일리지
    private String u_address;
    // 유저 주소
    private String u_name;
    // 유저 이름
    private String u_bank;
    // 은행명
    private String u_account;
    // 유저 계좌 번호
    private String u_description;
    // 유저 자기 소개
}
