package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.ArtworkPic;
import org.sopt.artoo.dto.Home;
import org.sopt.artoo.dto.Tag;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.service.HomeService;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class HomeController {
    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    /**
     * 홈 - 오늘의 작가
     * @return defaultRes
     */
    @GetMapping("/today")
    public ResponseEntity getAllTodayArtist(){
        try{
            DefaultRes<List<Home>> defaultRes = homeService.getAllTodayContents(); //작가 이름, 작가 사진들, 작품연도
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 홈 - 테마
     * @return defaultRes
     */
    @GetMapping("/themes")
    public ResponseEntity getAllTagInfo(){
        try{
            DefaultRes<List<Tag>> defaultRes = homeService.getAllTagInfo(); // 모든 Tag 정보

            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 홈 - 테마 - 테마 디테일
     * @return defaultRes
     */
    @GetMapping("/themes/details/{t_idx}")
    public ResponseEntity getAllDetailTagPic(@PathVariable("t_idx") final int t_idx){
        try{
            DefaultRes<List<ArtworkPic>> defaultRes = homeService.getAllTagPicUrl(t_idx); //Tag 모든 정보

            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
