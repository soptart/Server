package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.DisplayReq;
import org.sopt.artoo.service.DisplayContentService;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class DisplayContentController {
    private DisplayContentService displayContentService;
    private JwtService jwtService;

    public DisplayContentController(DisplayContentService displayContentService, JwtService jwtService) {
        this.displayContentService = displayContentService;
        this.jwtService = jwtService;
    }

    private static final DefaultRes UNAUTHORIZED_RES = new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);

    /**
     * 전시관람
     *
     * @param header      jwt token
     * @param display_idx 전시회 고유 인덱스
     * @return ResponseEntity - List<DisplayContentRes>
     */

    @GetMapping("/discontents/displays/{display_idx}")
    public ResponseEntity getByDisplayIdx(@RequestHeader(value = "Authorization", required = false) final String header,
                                          @PathVariable(value = "display_idx") final int display_idx) {
        try {
            return new ResponseEntity<>(displayContentService.findByDisplayIdx(display_idx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전시신청서
     *
     * @param header      jwt token
     * @return ResponseEntity - List<DisplayApplyRes>
     */
    @GetMapping("/discontents/application/{user_idx}")
    public ResponseEntity getDisplayApply(@RequestHeader(value = "Authorization", required = false) final String header,
                                          @PathVariable(value="user_idx") final int user_idx) {
        try {
            if(jwtService.checkAuth(header, user_idx)){
                final int u_idx = jwtService.decode(header).getUser_idx();
                return new ResponseEntity<>(displayContentService.findDisplayApply(u_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전시신청
     *
     * @param header     jwt token
     * @param displayReq 전시 컨텐츠
     * @return ResponseEntity
     */
//    @Auth
    @PostMapping("/discontents/{user_idx}")
    public ResponseEntity saveDisplayContent(@RequestHeader(value = "Authorization", required = false) final String header,
                                             @RequestBody final DisplayReq displayReq,
                                             @PathVariable(value="user_idx") final int user_idx) {
        try {
            if(jwtService.checkAuth(header, user_idx) && user_idx == displayReq.getU_idx() )
                return new ResponseEntity<>(displayContentService.save(displayReq), HttpStatus.OK);
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전시신청 취소
     *
     * @param header      jwt token
     * @param displaycontent_idx 전시 컨텐츠 고유 인덱스
     */
//    @Auth
    @DeleteMapping("/discontents/{displaycontent_idx}/users/{user_idx}")
    public ResponseEntity deleteDisplayContent(@RequestHeader(value = "Authorization", required = false) final String header,
                                               @PathVariable(value = "displaycontent_idx") final int displaycontent_idx,
                                               @PathVariable(value = "user_idx") final int user_idx) {
        try {
            if(jwtService.checkAuth(header, user_idx))
                return new ResponseEntity<>(displayContentService.deleteDisplaycontent(displaycontent_idx), HttpStatus.OK);
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

