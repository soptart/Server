package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.ArtworkLike;
import org.sopt.artoo.dto.MyArtwork;
import org.sopt.artoo.dto.Purchase;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.TransactionReq;
import org.sopt.artoo.model.UserSignUpReq;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.service.UserService;
import org.sopt.artoo.utils.auth.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.sopt.artoo.model.DefaultRes.FAIL_AUTHORIZATION_RES;
import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(final UserService userService, final JwtService jwtService){
        this.userService = userService;
        this.jwtService = jwtService;
    }


    /**
     * 유저의 미술품 보여주기
     * @param userIdx
     * @return List<artwork>
     */
    @GetMapping("/{u_idx}")
    public ResponseEntity getUserItem(
            @PathVariable("u_idx") final int userIdx){
        try {
            DefaultRes<List<MyArtwork>> defaultRes = userService.findUserWork(userIdx);
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
     * @return purchase
     */

    @GetMapping("/{u_idx}/likes") // 데이터 추가 후 확인
    public ResponseEntity getUserCollectionLike(
            @PathVariable("u_idx") final int userIdx) {
        try {
            DefaultRes<List<ArtworkLike>> defaultRes = userService.findUserLikes(userIdx);
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
    @GetMapping("/{u_idx}/purchase")
    public ResponseEntity getUserPurchase(
            @RequestHeader (value = "Authorization", required = false) final String header,
            @PathVariable("u_idx") final int userIdx) {
        if(jwtService.decode(header).getUser_idx()==userIdx) {
            try {
                DefaultRes<List<TransactionReq>> defaultRes = userService.findUserPurchase(userIdx);
                return new ResponseEntity<>(defaultRes, HttpStatus.OK);
            } catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity(FAIL_AUTHORIZATION_RES, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 유저별 거래 후기 모음
     *
     * @param userIdx
     * @return purchase
     */
    @GetMapping("/{u_idx}/review")
    public ResponseEntity getUserReview(
            @PathVariable("u_idx") final int userIdx) {
        try {
            DefaultRes<List<Purchase>> defaultRes = userService.findUserTransReview(userIdx);
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
     * 유저 소개란 조회
     *
     * @param userIdx
     * @return User.u_description
     */
    @GetMapping("/{u_idx}/description")
    public ResponseEntity getUserDescription(
            @PathVariable("u_idx") final int userIdx) {
        try {
            DefaultRes<String> defaultRes = userService.findUserDescription(userIdx);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 유저 소개란 삽입/수정
     *
     * @RequestBody userDescription
     * @param userIdx
     * @return User.u_description
     */
    @PostMapping("/{u_idx}/description")
    public ResponseEntity updateUserDescription(
            @RequestHeader (value = "Authorization", required = false) final String header,
            @RequestBody final String userDescription,
            @PathVariable("u_idx") final int userIdx) {
        if(jwtService.decode(header).getUser_idx()==userIdx) {
            try {
                DefaultRes<String> defaultRes = userService.updateUserDescription(userIdx, userDescription);
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
}
