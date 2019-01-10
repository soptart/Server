package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Purchase;
import org.sopt.artoo.mapper.ArtworkMapper;
import org.sopt.artoo.mapper.PurchaseMapper;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.PurchaseComment;
import org.sopt.artoo.service.DisplayService;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.service.NoticeService;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class NoticeController {
    private NoticeService noticeService;
    private JwtService jwtService;
    private DisplayService displayService;
    private PurchaseMapper purchaseMapper;
    private ArtworkMapper artworkMapper;
    private static final DefaultRes UNAUTHORIZED_RES = new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);

    public NoticeController(NoticeService noticeService, JwtService jwtService, DisplayService displayService, PurchaseMapper purchaseMapper,
                            ArtworkMapper artworkMapper) {
        this.noticeService = noticeService;
        this.jwtService = jwtService;
        this.displayService = displayService;
        this.purchaseMapper = purchaseMapper;
        this.artworkMapper = artworkMapper;
    }

    /**
     * 구매 내역 조회
     *
     * @param header   jwt token
     * @param user_idx 유저 idx
     * @return ResponseEntity - List<Display>
     */
    @GetMapping("/notices/buys/{user_idx}")
    public ResponseEntity getBuys(@RequestHeader(value = "Authorization", required = false) final String header,
                                  @PathVariable(value = "user_idx") final int user_idx) {
        try {
            //권한 체크
            if (jwtService.checkAuth(header, user_idx)) {
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
     * @param header   jwt token
     * @param user_idx 유저 idx
     * @return ResponseEntity - List<Display>
     */
    @GetMapping("/notices/sells/{user_idx}")
    public ResponseEntity getSells(@RequestHeader(value = "Authorization", required = false) final String header,
                                   @PathVariable(value = "user_idx") final int user_idx) {
        try {
            //권한 체크
            if (jwtService.checkAuth(header, user_idx)) {
                final int u_idx = jwtService.decode(header).getUser_idx();
                return new ResponseEntity<>(noticeService.findSellsByUidx(u_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 후기 작성
     *
     * @param header
     * @param p_idx
     * @param purchaseComment
     * @return localhost
     */
    @PostMapping("/notices/buys/{p_idx}")
    public ResponseEntity savePurchaseComment(@RequestHeader(value = "Authorization") final String header,
                                              @PathVariable(value = "p_idx") final int p_idx, @RequestBody PurchaseComment purchaseComment) {
        try {
            final int u_idx = jwtService.decode(header).getUser_idx();
            return new ResponseEntity<>(noticeService.trySavePurchaseComment(u_idx, p_idx, purchaseComment), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@DeleteMapping("/notices/{p_idx}/users/{u_idx}")
    public ResponseEntity refundPurchase(@RequestHeader(value = "Authorization") final String header,
                                         @PathVariable("p_idx") final int p_idx,
                                         @PathVariable("u_idx") final int u_idx) {
        if (jwtService.checkAuth(header, u_idx)) {

        }
    }*/

    /**
     * 	구매 환불
     *
     * @param header jwt token
     * @param u_idx  유저 idx
     * @param p_idx 구매 idx
     * @return ResponseEntity - List<Display>
     */
    @PutMapping("/notices/buys/{u_idx}/{p_idx}")
    public ResponseEntity requestRefund(@RequestHeader(value="Authorization" ,required = false) final String header,
                                     @PathVariable(value="u_idx") final int u_idx,
                                     @PathVariable(value="p_idx") final int p_idx){
        try {
            //권한 체크
            if(jwtService.checkAuth(header, u_idx)){
                return new ResponseEntity<>(noticeService.requestRefund(p_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전시내역 조회
     *
     * @param header   jwt token
     * @param user_idx 유저 idx
     * @return ResponseEntity - List<Display>
     */
    @GetMapping("/notices/displays/users/{user_idx}")
    public ResponseEntity getNoticeDisplayApply(@RequestHeader(value = "Authorization", required = false) final String header,
                                                @PathVariable(value = "user_idx") final int user_idx) {
        try {
            //권한 체크
            if (jwtService.checkAuth(header, user_idx)) {
                final int u_idx = jwtService.decode(header).getUser_idx();
                return new ResponseEntity<>(noticeService.findNoticeDisplayApply(u_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
