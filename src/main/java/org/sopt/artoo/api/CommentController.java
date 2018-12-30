package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Comment;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.service.CommentService;
import org.sopt.artoo.service.JwtService;
import org.sopt.artoo.service.UserService;
import org.sopt.artoo.utils.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
            // useridx로 유저가 쓴 코멘트들을 가져와서 그 코멘트 중에 이 artwork에 artwork가 가지고 있는 c_idx를 가진 코멘트가 있는지 확인
            DefaultRes<List<Comment>> commentList = commentService.findAllCommentByArtIdx(a_idx);
            if(commentList.getData().stream().filter(comment -> comment.getU_idx() == userIdx).collect(Collectors.toList()).size()!=0){
                for (Comment comment : commentList.getData()) {
                    comment.setAuth(userIdx == comment.getU_idx());
                }
                return new ResponseEntity<>(commentList, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(ResponseMessage.INDEX_NOT_FOUNDED, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
