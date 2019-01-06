package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.service.ArtworkService;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AdminController {
    private static final DefaultRes UNAUTHORIZED_RES = new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);

    private final ArtworkService artworkService;
    private final JwtService jwtService;

    public AdminController(ArtworkService artworkService, JwtService jwtService) {
        this.artworkService = artworkService;
        this.jwtService = jwtService;
    }





}
