package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.service.DisplayService;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.service.NoticeService;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class NoticeController {
    private NoticeService noticeService;
    private JwtService jwtService;
    private DisplayService displayService;
    private static final DefaultRes UNAUTHORIZED_RES = new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);

    public NoticeController(NoticeService noticeService, JwtService jwtService, DisplayService displayService) {
        this.noticeService = noticeService;
        this.jwtService = jwtService;
        this.displayService = displayService;
    }

    /**
     * 구매 내역 조회

     *
     * @param header     jwt token
     * @param user_idx  유저 idx
     * @return ResponseEntity - List<Display>
     */
    @GetMapping("/notices/buys/{user_idx}")
    public ResponseEntity getBuys(@RequestHeader(value="Authorization" ,required = false) final String header,
                                     @PathVariable(value="user_idx") final int user_idx){
        try {
            //권한 체크
            if(jwtService.checkAuth(header, user_idx)){
                final int u_idx = jwtService.decode(header).getUser_idx();
                log.info(String.valueOf(u_idx));
                return new ResponseEntity<>(noticeService.findBuysByUidx(u_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 판매 내역 조회
     *
     * @param header     jwt token
     * @param user_idx  유저 idx
     * @return ResponseEntity - List<Display>
     */
    @GetMapping("/notices/sells/{user_idx}")
    public ResponseEntity getSells(@RequestHeader(value="Authorization" ,required = false) final String header,
                                     @PathVariable(value="user_idx") final int user_idx){
        try {
            //권한 체크
            if(jwtService.checkAuth(header, user_idx)){
                final int u_idx = jwtService.decode(header).getUser_idx();
                return new ResponseEntity<>(noticeService.findSellsByUidx(u_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    /**
//     * 	후기 작성
//     *
//     * @param header     jwt token
//     * @param user_idx  유저 idx
//     * @return ResponseEntity - List<Display>
//     */
//    @PostMapping("/notices/buys/{purchase_idx}/comments/{user_idx}")
//    public ResponseEntity insertPurchaseComment(@RequestHeader(value="Authorization" ,required = false) final String header,
//                                     @PathVariable(value="user_idx") final int user_idx,
//                                     @PathVariable(value="purchase_idx") final int purchase_idx){
//        try {
//            //권한 체크
//            if(jwtService.checkAuth(header, user_idx)){
//                final int u_idx = jwtService.decode(header).getUser_idx();
//                return new ResponseEntity<>(noticeService.savePurchaseComment(u_idx, purchase_idx), HttpStatus.OK);
//            }
//            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    /**
//     * 	구매 환불
//     *
//     * @param header     jwt token
//     * @param user_idx  유저 idx
//     * @return ResponseEntity - List<Display>
//     */
//    @DeleteMapping("/notices/{purchase_idx}/users/{user_idx}")
//    public ResponseEntity getDisplay(@RequestHeader(value="Authorization" ,required = false) final String header,
//                                     @PathVariable(value="user_idx") final int user_idx,
//                                     @PathVariable(value="purchase_idx") final int purchase_idx){
//        try {
//            //권한 체크
//            if(jwtService.checkAuth(header, user_idx)){
//                final int u_idx = jwtService.decode(header).getUser_idx();
//                return new ResponseEntity<>(noticeService.deletePurchaseComment(u_idx, purchase_idx), HttpStatus.OK);
//            }
//            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



//    /**
//     * 전시내역 조회
//     *
//     * @param header     jwt token
//     * @param user_idx  유저 idx
//     * @return ResponseEntity - List<Display>
//     */
//    @GetMapping("/notices/displays/users/{user_idx}}")
//    public ResponseEntity getSells(@RequestHeader(value="Authorization" ,required = false) final String header,
//                                   @PathVariable(value="user_idx") final int user_idx){
//        try {
//            //권한 체크
//            if(jwtService.checkAuth(header, user_idx)){
//                final int u_idx = jwtService.decode(header).getUser_idx();
//                return new ResponseEntity<>(noticeService.findSellsByUidx(u_idx), HttpStatus.OK);
//            }
//            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
