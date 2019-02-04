package org.sopt.artoo.utils;

import org.sopt.artoo.dto.KakaoJson;

public class KakaoAccessToken {
    public static KakaoJson getKakaoAccessToken(String code){
        final String RequestUrl = "https://kauth.kakao.com/oauth/token";
        KakaoJson kakaoJson = new KakaoJson("authorization_code", "9477313f9acbc65bfe8d30cf67cc3802",
                "http://localhost:8080/MS/kakaologin", code);
        return kakaoJson;
    }
}
