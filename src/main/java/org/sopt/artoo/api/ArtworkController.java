package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.dto.PurchaseProduct;
import org.sopt.artoo.model.ArtworkFilterReq;
import org.sopt.artoo.model.ArtworkReq;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.PurchaseReq;
import org.sopt.artoo.service.ArtworkService;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.sopt.artoo.utils.auth.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.sopt.artoo.model.DefaultRes.FAIL_AUTHORIZATION_RES;
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
     * 미술작품 구매
     *
     * @param header      jwt token
     * @param a_idx       미술작품 고유 번호
     * @param u_idx       구매자 고유 번호
     * @param purchaseReq 구매 요구 정보
     * @return ResponseEntity
     */
    @Auth
    @PostMapping("/artworks/{a_idx}/purchase/{u_idx}")
    public ResponseEntity buyArtwork(
            @RequestHeader(value = "Authorization", required = false) final String header,
            @PathVariable("a_idx") final int a_idx,
            @PathVariable("u_idx") final int u_idx,
            @RequestBody PurchaseReq purchaseReq) {
        if (jwtService.decode(header).getUser_idx() == u_idx) {
            try {
                DefaultRes<PurchaseProduct> defaultRes = artworkService.purchaseArtwork(u_idx, a_idx, purchaseReq);
                return new ResponseEntity<>(defaultRes, HttpStatus.OK);
            } catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity(FAIL_AUTHORIZATION_RES, HttpStatus.UNAUTHORIZED);
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
            final ArtworkReq artworkReq, final MultipartFile picUrl) {
        try {
            artworkReq.setU_idx(jwtService.decode(header).getUser_idx());
            artworkReq.setPic_url(picUrl);
            artworkReq.setA_size(calculateSize(artworkReq));
            return new ResponseEntity<>(artworkService.save(artworkReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param header     jwt token
     *
     * @param artworkReq 미술작품 데이터
     * @return
     */

    @Auth
    @PutMapping("/artworks")
    public ResponseEntity updateArtwork(
            @RequestHeader(value = "Authorization") final String header,
            final ArtworkReq artworkReq, final MultipartFile picUrl) {
        try {
            log.info(picUrl.toString());
            if (picUrl.isEmpty()){
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.ARTWORK_NOPICUTRE), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            artworkReq.setPic_url(picUrl);
            artworkReq.setA_size(calculateSize(artworkReq));
            final int useridx = jwtService.decode(header).getUser_idx();
            log.info("userIdx"+String.valueOf(useridx));
            log.info("artworkIdx"+String.valueOf(artworkReq.getA_idx()));
            if (artworkService.checkAuth(useridx, artworkReq.getA_idx()))
                return new ResponseEntity<>(artworkService.update(artworkReq), HttpStatus.OK);
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 미술 작품 필터
     * @param artworkFilterReq
     */
    @GetMapping("/artworks/filter")
    public ResponseEntity filterArtwork(
            @RequestBody final ArtworkFilterReq artworkFilterReq) {
        try {
            DefaultRes defaultRes = artworkService.filterArtworkPic(artworkFilterReq); //작가 이름, 작가 사진들, 작품연도
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

  
    /**
     * 작품에 대한 좋아요 수 조회
     *
     * @param a_idx 작품 고유 번호
     * @return a_like_count 좋아요 수
     */
    @GetMapping("/artworks/{a_idx}/likes")
    public ResponseEntity getArtworkLikes(
            @PathVariable("a_idx") final int a_idx) {
        try {
            return new ResponseEntity<>(artworkService.getLikecountByArtIdx(a_idx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 작품에 좋아요 선택
     *
     * @param a_idx 작품 고유 번호
     * @return artwork 작품
     */
    @Auth
    @PostMapping("/artworks/{a_idx}/likes")
    public ResponseEntity like(
            @RequestHeader(value = "Authorization") final String header,
            @PathVariable("a_idx") final int a_idx) {
        try {
            final int u_idx = jwtService.decode(header).getUser_idx();
            return new ResponseEntity<>(artworkService.saveArtworkLike(a_idx, u_idx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/artwork/delete/{a_idx}")
    public ResponseEntity deleteArtwork(
            @RequestHeader(value = "Authorization") final String header,
            @PathVariable("a_idx") final int a_idx){
        try {
            if(jwtService.decode(header).getUser_idx() == artworkService.findByArtIdx(a_idx).getData().getU_idx()){
                return new ResponseEntity<>(artworkService.deleteByArtIdx(a_idx), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Integer calculateSize(ArtworkReq artworkReq){
        final int width = artworkReq.getA_width();
        final int height = artworkReq.getA_height();
        final int depth = artworkReq.getA_depth();

        int list[] = {width, height, depth};
        Arrays.sort(list);

        return list[2] * list[1];
    }
}
