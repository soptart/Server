package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.User;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.UserSignUpReq;
import org.sopt.artoo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService){
        this.userService = userService;
    }
    //회원 가입
    @PostMapping("")
    public ResponseEntity userSignUp(@RequestBody UserSignUpReq userSignUpReq){
        try {
            return new ResponseEntity<>(userService.save(userSignUpReq), HttpStatus.OK);
        } catch {
            //로그 안찍혀서 다시해야함
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //개인 작품 확인, jwt 삽입 해야함!! -> 이후 작업 연결할 Service 하고 Mapper 작업 만들어야함
    @GetMapping("/{u_idx}")
    public ResponseEntity getUserItem(
            //@RequestHeader (value = "Authorization", required = false) final String header, 인증키 추후 수정
            @PathVariable("u_idx") final int userIdx){
        try {
            DefaultRes<User> defaultRes = userService.findUserWork(userIdx);
            //if (jwtService.checkAuth(header, userIdx)) defaultRes.getData().setAuth(true); 인증 키 추후 수정
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch {
            //로그 안찍혀서 다시해야함
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
