package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.model.ArtworkReq;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.service.ArtworkService;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.sopt.artoo.utils.auth.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class ArtworkController {

    private static final DefaultRes UNAUTHORIZED_RES = new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);

    private final ArtworkService artworkService;
    private final JwtService jwtService;

    public ArtworkController(ArtworkService artworkService, JwtService jwtService) {
        this.artworkService = artworkService;
        this.jwtService = jwtService;
    }

    /**
     * 미술작품 전체 불러오기
     *
     * @param header jwt token
     * @return ResponseEntity
     */
    @GetMapping("/artworks")
    public ResponseEntity getAllartworks(
            @RequestHeader(value = "Authorization", required = false) final String header
    ) {
        try {
            final int userIdx = jwtService.decode(header).getUser_idx();
            DefaultRes<List<Artwork>> defaultRes = artworkService.findAll();

            for (Artwork artwork : defaultRes.getData()) {
                artwork.setAuth(userIdx == artwork.getU_idx());
            }
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 미술작품 조회
     *
     * @param header jwt token
     * @param a_idx  미술작품 고유 번호
     * @return ResponseEntity
     */
    @GetMapping("/artworks/{a_idx}")
    public ResponseEntity getArtwork(
            @RequestHeader(value = "Authorization", required = false) final String header,
            @PathVariable("a_idx") final int a_idx) {
        try {
            final int userIdx = jwtService.decode(header).getUser_idx();
            DefaultRes<Artwork> defaultRes = artworkService.findByArtIdx(a_idx);
            defaultRes.getData().setAuth(userIdx == defaultRes.getData().getU_idx());
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 미술작품 작성
     *
     * @param header
     * @param artworkReq
     * @return ResponseEntity
     */
    @Auth
    @PostMapping("/artworks")
    public ResponseEntity saveArtwork(
            @RequestHeader(value = "Authorization") final String header,
            ArtworkReq artworkReq, final MultipartFile picUrl) {
        try {
            //artworkReq.setU_idx(jwtService.decode(header).getUser_idx());
            artworkReq.setU_idx(1);
            artworkReq.setPic_url(picUrl);
            return new ResponseEntity<>(artworkService.save(artworkReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param header     jwt token
     * @param a_idx      미술작품 인덱스
     * @param artworkReq 미술작품 데이터
     * @return
     */
    @Auth
    @PutMapping("/artworks/{a_idx}")
    public ResponseEntity updateArtwork(
            @RequestHeader(value = "Authorization") final String header,
            @PathVariable("a_idx") final int a_idx,
            ArtworkReq artworkReq) {
        try {
            artworkReq.setA_idx(a_idx);
            return new ResponseEntity<>(artworkService.update(artworkReq), HttpStatus.OK); // 여기 지우면 됨
            /*if (artworkService.checkAuth(jwtService.decode(header).getUser_idx(), a_idx))
                return new ResponseEntity<>(artworkService.update(artworkReq), HttpStatus.OK);
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);*/
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/artworks/{a_idx}")
    public ResponseEntity deleteArtwork(
            @RequestHeader(value = "Authorization") final String header,
            @PathVariable("a_idx") final int a_idx) {
        try {
            return new ResponseEntity<>(artworkService.deleteByArtIdx(a_idx), HttpStatus.OK);
            //임시 주석
            /*if(artworkService.checkAuth(jwtService.decode(header).getUser_idx(), a_idx))
                return new ResponseEntity<>(artworkService.deleteByArtIdx(a_idx), HttpStatus.OK);
            return new ResponseEntity<>(UNAUTHORIZED_RES,HttpStatus.OK);*/
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
