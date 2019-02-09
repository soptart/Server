package org.sopt.artoo.model;

import lombok.Data;


@Data
public class ExternalLoginReq {
    public final static int original = 0;
    public final static int Kakao = 1;
    public final static int Naver = 2;
    private String accessToken;
    private int loginType;
}
