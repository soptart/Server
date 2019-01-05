package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Display;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.DisplayAddReq;
import org.sopt.artoo.model.DisplayReq;
import org.sopt.artoo.service.DisplayService;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    private static final DefaultRes UNAUTHORIZED_RES = new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);


    /**
     * 전시 메인 - 모든 전시 조회
     *
     * @param header     jwt token
     * @return ResponseEntity - List<Display>
     */
    @GetMapping("/displays")
    public ResponseEntity getDisplay(@RequestHeader(value="Authorization" ,required = false) final String header){
        try {
            return new ResponseEntity<>(displayService.findDisplays(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전시장 입장
     *
     * @param header     jwt token
     * @param display_idx  전시장 고유 id
     * @return ResponseEntity - <Display>
     */
    @GetMapping("/displays/{display_idx}")
    public ResponseEntity getDisplay(@RequestHeader(value="Authorization",  required = false) final String header,
                                     @PathVariable(value="display_idx") final int display_idx){
        try {
            return new ResponseEntity<>(displayService.findByDisplayIdx(display_idx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 전시장 추가
     * 관리자만 추가할 수 있음
     */
    @PostMapping("/displays")
    public ResponseEntity saveDisplay(@RequestHeader(value="Authorization", required = false) final String header,
                                      final DisplayAddReq displayAddReq, final MultipartFile repImg_url,
                                      final MultipartFile titleImg_url, final MultipartFile mainImg_url){
        try {
//            if(jwtService.decode(header).getUser_idx() == ) { //관리자 u_idx갖고오는 함수
                if (repImg_url != null) {
                    displayAddReq.setM_d_repImg_url(repImg_url);
                }
                if (mainImg_url != null) {
                    displayAddReq.setM_d_mainImg_url(mainImg_url);
                }
                if (titleImg_url != null) {
                    displayAddReq.setM_d_titleImg_url(titleImg_url);
                }
                return new ResponseEntity<>(displayService.addDisplay(displayAddReq), HttpStatus.OK);
//            }
//            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());

            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 전시 수정 - 관리자
     */
    @PutMapping("/displays")
    public ResponseEntity updateDisplay(@RequestHeader(value="Authorization",  required = false) final String header,
                                     final DisplayAddReq displayAddReq, final MultipartFile repImg_url,
                                        final MultipartFile titleImg_url, final MultipartFile mainImg_url){
        try {
            //관리자가
            //if(jwtService.decode(header).getUser_idx() == ) { //관리자 u_idx갖고오는 함수
            //                if (repImg_url != null) {
            if (repImg_url != null) {
                displayAddReq.setM_d_repImg_url(repImg_url);
            }
            if (mainImg_url != null) {
                displayAddReq.setM_d_mainImg_url(mainImg_url);
            }
            if (titleImg_url != null) {
                displayAddReq.setM_d_titleImg_url(titleImg_url);
            }
            return new ResponseEntity<>(displayService.updateDisplay(displayAddReq), HttpStatus.OK);
            //            }
//            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}