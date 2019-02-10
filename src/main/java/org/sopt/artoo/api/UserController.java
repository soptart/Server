package org.sopt.artoo.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.MyPageRes;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.UserDescriptionReq;
import org.sopt.artoo.model.UserPwInfo;
import org.sopt.artoo.model.UserSignUpReq;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.service.KakaoService;
import org.sopt.artoo.service.UserService;
import org.sopt.artoo.utils.auth.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.sopt.artoo.model.DefaultRes.FAIL_AUTHORIZATION_RES;
import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;
import static org.sopt.artoo.model.DefaultRes.FAIL_FIND_USER;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final KakaoService kakaoService;

    public UserController(final UserService userService, final JwtService jwtService, final KakaoService kakaoService){
        this.userService = userService;
        this.jwtService = jwtService;
        this.kakaoService = kakaoService;
    }

    @PostMapping("/u_email/{u_email}")
    public ResponseEntity getUserEmail(
            @PathVariable("u_email") final String email){
        DefaultRes defaultRes = userService.findUserEmailExist(email);
        return new ResponseEntity<>(defaultRes, HttpStatus.OK);
    }


    /**
     * 유저의 미술품 보여주기
     * @param userIdx
     * @return MyPageRes
     */
    @GetMapping("/{u_idx}")
    public ResponseEntity getUserItem(
            @PathVariable("u_idx") final int userIdx){
        try {
            MyPageRes defaultRes = userService.findUserWork(userIdx);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 유저가 좋아요 클릭한 현황 조회
     *
     * @param userIdx
     * @return MyPageRes
     */

    @GetMapping("/{u_idx}/likes") // 데이터 추가 후 확인
    public ResponseEntity getUserCollectionLike(
            @PathVariable("u_idx") final int userIdx) {
        try {
            MyPageRes defaultRes = userService.findUserLikes(userIdx);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 구매 현황 조회
     *
     * @param userIdx
     * @return purchase
     */

    @Auth
    @GetMapping("/{u_idx}/purchases")
    public ResponseEntity getUserPurchase(
            @PathVariable("u_idx") final int userIdx) {
            try {
                return new ResponseEntity<>(userService.findUserPurchase(userIdx), HttpStatus.OK);
            } catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    /**
     * 유저별 거래 후기 모음
     *
     * @param userIdx
     * @return purchase
     */
    @GetMapping("/{u_idx}/reviews")
    public ResponseEntity getUserReview(
            @PathVariable("u_idx") final int userIdx) {
        try {
            MyPageRes defaultRes = userService.findUserTransReview(userIdx);
            if(defaultRes.getDataNum() == -1){
                DefaultRes errorRes = DefaultRes.res(defaultRes.getStatus(),defaultRes.getMessage());
                return new ResponseEntity<>(errorRes, HttpStatus.OK);
            }
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 회원 가입
     * @Request userSignUpReq
     * @return  none
     */
    @PostMapping("")
    public ResponseEntity userSignUp(@RequestBody UserSignUpReq userSignUpReq){
        try {
            return new ResponseEntity<>(userService.save(userSignUpReq), HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 외부 회원 가입입니다.
     * @param userSignUpReq
     * @return
     */
    @PostMapping("/external")
    public ResponseEntity userSignUpExternal(@RequestBody final UserSignUpReq userSignUpReq){
        if(userSignUpReq.getU_type() == UserSignUpReq.Kakao) {
            JsonNode userInfo = kakaoService.verifyAccessToken(userSignUpReq.getAccessToken());
            //우선은 id로 해놨는데 나중에 email로 바꿔도 되는건지 확인해봅시당
            if (!userInfo.path("id").isMissingNode()) {
                userSignUpReq.setExternal_key(userInfo.path("id").asInt());
                if (userService.findByUserIdAndType(userSignUpReq.getExternal_key(), userSignUpReq.getU_type())) {
                    return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.BAD_REQUEST);
                }
                JsonNode kakao_account = userInfo.path("kakao_account");
                if (kakao_account.path("has_email").asBoolean() && kakao_account.path("is_email_valid").asBoolean() && kakao_account.path("is_email_verified").asBoolean()) {
                    userSignUpReq.setU_email(kakao_account.path("email").asText());
                }
                JsonNode properties = userInfo.path("properties");
                if(properties.path("nickname").asText()!=null){
                    userSignUpReq.setU_name(userInfo.path("nickname").asText());
                    log.info("nickname: "+userInfo.path("nickname").asText());
                }else{
                    userSignUpReq.setU_name("no_name");
                    log.info("no_name");
                }
            } else {
                return new ResponseEntity<>(FAIL_FIND_USER, HttpStatus.BAD_REQUEST);
            }
            try {
                userSignUpReq.setU_type(UserSignUpReq.Kakao);
                return new ResponseEntity<>(userService.saveExternal(userSignUpReq), HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("failed set u_type");
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 유저 소개란 삽입/수정
     *
     * @RequestBody userDescription
     * @param userIdx
     * @return User.u_description
     */
    @PutMapping("/{u_idx}/descriptions")
    public ResponseEntity updateUserDescription(
            @RequestHeader (value = "Authorization", required = false) final String header,
            @RequestBody final UserDescriptionReq userDescriptionReq,
            @PathVariable("u_idx") final int userIdx) {
        if(jwtService.decode(header).getUser_idx()==userIdx) {
            try {
                DefaultRes defaultRes = userService.updateUserDescription(userIdx, userDescriptionReq);
                return new ResponseEntity<>(defaultRes, HttpStatus.OK);
            } catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            return new ResponseEntity(FAIL_AUTHORIZATION_RES, HttpStatus.UNAUTHORIZED);
        }
    }





    /**
     *
     * @param userIdx
     * @return User 객체
     */
    @GetMapping("/{u_idx}/myInfo")
    public ResponseEntity getUserInfo(
            @PathVariable("u_idx") final int userIdx) {

            try {
                DefaultRes defaultRes = userService.findUser(userIdx);
                return new ResponseEntity<>(defaultRes, HttpStatus.OK);
            } catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

        /******* 유저 정보 변경 *******/

    /**
     * 비밀 번호를 제외한 회원 정보 수정
     * @param header
     * @param userIdx
     * @return
     */
    @PutMapping("/{u_idx}/myInfo")
    public ResponseEntity updateUser(
            @RequestHeader (value = "Authorization", required = false) final String header,
            @RequestBody UserSignUpReq userInfo,
            @PathVariable("u_idx") final int userIdx) {
        if (jwtService.checkAuth(header, userIdx)) {
            try {
                DefaultRes defaultRes = userService.changeUserInfo(userIdx, userInfo);
                return new ResponseEntity<>(defaultRes, HttpStatus.OK);
            } catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity(FAIL_AUTHORIZATION_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 회원 비밀번호 수정
     * @param header
     * @param userPwInfo
     * @param userIdx
     */
    @PutMapping("/{u_idx}/myInfo/pw")
    public ResponseEntity updateUserPw(
            @RequestHeader (value = "Authorization", required = false) final String header,
            @RequestBody final UserPwInfo userPwInfo,
            @PathVariable("u_idx") final int userIdx) {
        if (jwtService.checkAuth(header, userIdx)) {
            try {
                DefaultRes defaultRes = userService.userPwChange(userIdx, userPwInfo);
                return new ResponseEntity<>(defaultRes, HttpStatus.OK);
            } catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity(FAIL_AUTHORIZATION_RES, HttpStatus.UNAUTHORIZED);
        }
    }
}

