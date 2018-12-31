package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Comment;
import org.sopt.artoo.model.CommentReq;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.service.CommentService;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.service.UserService;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.sopt.artoo.utils.auth.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;
    private final UserService userService;

    public CommentController(CommentService commentService, JwtService jwtService, UserService userService) {
        this.commentService = commentService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/comments/{a_idx}")
    public ResponseEntity findAllCommentsByArtIdx(
            @RequestHeader(value = "Authorization", required = false) final String header,
            @PathVariable final int a_idx) {
        try {
            //토큰으로 유저인덱스 가져오기
            final int userIdx = jwtService.decode(header).getUser_idx();
            // userIdx로 유저가 쓴 코멘트들을 가져와서 그 코멘트 중에 이 artwork에 artwork가 가지고 있는 c_idx를 가진 코멘트가 있는지 확인
            DefaultRes<List<Comment>> commentList = commentService.findAllCommentByArtIdx(a_idx);
            if (commentList.getData().stream().filter(comment -> comment.getU_idx() == userIdx).collect(Collectors.toList()).size() != 0) {
                for (Comment comment : commentList.getData()) {
                    comment.setAuth(userIdx == comment.getU_idx());
                }
                return new ResponseEntity<>(commentList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.INDEX_NOT_FOUNDED), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("/comments")
    public ResponseEntity saveComment(
            @RequestHeader(value = "Authorization") final String header,
            @RequestBody final CommentReq commentReq) {
        // 토큰으로 유저인덱스 가져오기
        try {
            final int userIdx = jwtService.decode(header).getUser_idx();
            if(userService.findUser(userIdx)!=null){
                return new ResponseEntity<>(commentService.saveComment(commentReq), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
