package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.model.DisplayReq;
import org.sopt.artoo.service.DisplayService;
import org.sopt.artoo.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class DisplayController {
    private DisplayService displayService;
    private JwtService jwtService;

    public DisplayController(DisplayService displayService, JwtService jwtService) {
        this.displayService = displayService;
        this.jwtService = jwtService;
    }

    /**
     * 모든 전시 조회 - 전시 메인
     *
     * @param header     jwt token
     * @return ResponseEntity
     */
    @GetMapping("/display")
    public ResponseEntity getDisplay(@RequestHeader(value="Authorization") final String header){
        try {
            return new ResponseEntity<>(displayService.findDisplays(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전시회 관람
     *
     * @param header     jwt token
     * @param displayIdx 전시 고유 인덱스
     * @return ResponseEntity
     */
    @GetMapping("/display/{displayIdx}")
    public ResponseEntity getByDisplayIdx(@RequestHeader(value="Authorization") final String header,
                                          @PathVariable(value="displayIdx") final int displayIdx){
        try {
            return new ResponseEntity<>(displayService.findByDisplayIdx(displayIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전시신청
     *
     * @param header     jwt token
     * @param displayIdx 전시 고유 인덱스
     * @return ResponseEntity
     */
//    @Auth
    @PostMapping("/display")
    public ResponseEntity save(@RequestHeader(value="Authorization") final String header,
                               final DisplayReq displayReq){
        try {
            displayReq.setU_idx(jwtService.decode(header).getUser_idx());
            return new ResponseEntity<>(displayService.save(displayReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전시신청 취소
     *
     * @param header     jwt token
     * @param displayIdx 전시 고유 인덱스
     */
    @DeleteMapping("/display/{display_idx}")
    public ResponseEntity update(@RequestHeader(value="Authorization") final String header,
                                 @PathVariable(value="display_idx") final int display_idx,
                                 final int displayIdx){
        try {
            final int userIdx = jwtService.decode(header).getUser_idx();

            return new ResponseEntity<>(displayService.save(displayReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}