package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.LoginReq;
import org.sopt.artoo.model.PurchaseReq;
import org.sopt.artoo.service.*;
import org.sopt.artoo.utils.PasswordIncoder;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.sopt.artoo.utils.auth.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class AdminController {
    private static final DefaultRes FAIL_DEFAULT_RES = new DefaultRes(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);

    private final AdminService adminService;
    private final JwtService jwtService;
    private final DisplayContentService displayContentService;
    private final DisplayService displayService;

    public AdminController(AdminService adminService, JwtService jwtService, DisplayContentService displayContentService, DisplayService displayService) {
        this.adminService = adminService;
        this.jwtService = jwtService;
        this.displayContentService = displayContentService;
        this.displayService = displayService;
    }
    private static final DefaultRes UNAUTHORIZED_RES = new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);

    /**
     * Admin Login
     * @param loginReq
     */
    @PostMapping("/admin/login")
    public ResponseEntity adminLogin(@RequestBody final LoginReq loginReq) {
        try {
            String hashpw = PasswordIncoder.incodePw(loginReq.getU_pw());
            loginReq.setU_pw(hashpw);
            return new ResponseEntity<>(adminService.adminLogin(loginReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 유저 ID로 거래 내역 요청
     * @param
     * @param header
     * @return
     */
    @GetMapping("/admin/users/email/{u_email}")
    public ResponseEntity userFind(@PathVariable final String u_email,
                                   @RequestHeader (value ="Authorization", required = false) final String header){
        if(jwtService.decode(header).getUser_idx() == 0){
            try {
                return new ResponseEntity<>(adminService.findUserPurchaseById(u_email), HttpStatus.OK);
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 특정 인덱스에 해당하는 구매 내역 호출
     * @param p_idx
     * @param header
     * @return
     */
    @GetMapping("/admin/users/{p_idx}")
    public ResponseEntity findPurchaseState(@PathVariable("p_idx") final int p_idx,
                                            @RequestHeader (value ="Authorization", required = false) final String header){
        if(jwtService.decode(header).getUser_idx() == 0){
            try {
                return new ResponseEntity<>(adminService.findPurchaseByPidx(p_idx), HttpStatus.OK);
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
    }


    @PostMapping("/admin/users/{p_idx}")
    public ResponseEntity updatePurchaseState(@PathVariable("p_idx") final int p_idx,
                                              @RequestBody final int p_state,
                                              @RequestHeader (value ="Authorization", required = false) final String header){
        if(jwtService.decode(header).getUser_idx() == 0){
            try {
                return new ResponseEntity<>(adminService.updatePurchaseState(p_idx, p_state), HttpStatus.OK);
            }catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 관리자 - 모든 전시회 반환
     *
     *
     * @param header jwt token
     * @return ResponseEntity
     */
    @GetMapping("/admin/displays")
    public ResponseEntity getDisplayApply(@RequestHeader(value = "Authorization", required = false) final String header) {
        try {
            if(jwtService.decode(header).getUser_idx() == 0){
                return new ResponseEntity<>(displayService.findAllDisplays(), HttpStatus.OK);
            }
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 관리자 - 전시 신청 작품 목룍 조회
     *
     *
     * @param header jwt token
     * @return ResponseEntity
     */
    @GetMapping("/admin/displays/{display_idx}")
    public ResponseEntity getDisplayApply(@RequestHeader(value = "Authorization", required = false) final String header,
                                          @PathVariable(value="display_idx") final int display_idx) {
        try {
            if(jwtService.decode(header).getUser_idx() == 0){
                return new ResponseEntity<>(displayContentService.findByDisplayIdx(display_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * 관리자 - 전시 신청 작품 삭제
     *
     *
     * @param header jwt token
     * @return ResponseEntity
     */
    @Auth
    @DeleteMapping("/admin/discontents/{displayContent_idx}")
    public ResponseEntity deleteComment(
            @RequestHeader(value = "Authorization") final String header,
            @PathVariable("displayContent_idx") final int displayContent_idx) {
        try {
            if(jwtService.decode(header).getUser_idx() == 0){
                return new ResponseEntity<>(displayContentService.deleteDisplaycontent(displayContent_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
