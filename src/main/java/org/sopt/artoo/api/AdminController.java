package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.LoginReq;
import org.sopt.artoo.model.PurchaseReq;
import org.sopt.artoo.service.AdminService;
import org.sopt.artoo.service.ArtworkService;
import org.sopt.artoo.service.AuthService;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class AdminController {
    private static final DefaultRes FAIL_DEFAULT_RES = new DefaultRes(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);

    private final AdminService adminService;
    private final JwtService jwtService;

    public AdminController(AdminService adminService, JwtService jwtService) {
        this.adminService = adminService;
        this.jwtService = jwtService;
    }

    /**
     * Admin Login
     * @param loginReq
     */
    @PostMapping("/admin/login")
    public ResponseEntity adminLogin(@RequestBody final LoginReq loginReq) {
        try {
            return new ResponseEntity<>(adminService.adminLogin(loginReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 유저 ID로 거래 내역 요청
     * @param loginReq
     * @param header
     * @return
     */
    @GetMapping("/admin/users")
    public ResponseEntity userFind(@RequestBody final LoginReq loginReq,
                                   @RequestHeader (value ="Authorization", required = false) final String header){
        if(jwtService.decode(header).getUser_idx() == 0){
            try {
                return new ResponseEntity<>(adminService.findUserPurchaseById(loginReq), HttpStatus.OK);
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
}
