package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.User;
import org.sopt.artoo.mapper.UserMapper;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.LoginReq;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import org.sopt.artoo.model.ExternalLoginReq;

@Service
@Slf4j
public class AuthService {
    private final UserMapper userMapper;

    private final JwtService jwtService;
    private final KakaoService kakaoService;

    public AuthService(final UserMapper userMapper, JwtService jwtService, KakaoService kakaoService) {
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.kakaoService = kakaoService;
    }

    /**
     * 로그인 서비스
     * @param loginReq 로그인 객체
     * @return DefaultRes
     */
    public DefaultRes<JwtService.TokenRes> login(final LoginReq loginReq) {
        final User user = userMapper.findByIdAndPassword(loginReq);
        if (user != null) {
            //토큰 생성
            final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(user.getU_idx()), user.getU_idx());
            return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenDto);
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
    }


    public DefaultRes<JwtService.TokenRes> loginExternal(final ExternalLoginReq loginReq) {
        int userId = -1;
        if(loginReq.getLoginType() == ExternalLoginReq.Kakao){
            JsonNode userInfo = kakaoService.verifyAccessToken(loginReq.getAccessToken());
            if(!userInfo.path("id").isMissingNode()){
                userId = userInfo.path("id").asInt();
            }else {
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
            }
        }else{
            log.info("no kakao");
        }
        final User user = userMapper.findByUserIdAndType(userId, loginReq.getLoginType());
        if(user!=null){
            int userIdx = user.getU_idx();
            final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(userIdx));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenDto);
        }
        return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.LOGIN_FAIL);
    }
}
