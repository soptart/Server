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


}