package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.service.ArtworkService;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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

            }
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
