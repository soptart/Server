package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.DisplayContent;
import org.sopt.artoo.mapper.ArtworkPicMapper;
import org.sopt.artoo.mapper.DisplayContentMapper;
import org.sopt.artoo.mapper.DisplayMapper;
import org.sopt.artoo.mapper.UserMapper;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.DisplayReq;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
public class DisplayContentService {
    private DisplayContentMapper displayContentMapper;
    private UserMapper userMapper;
    private ArtworkPicMapper artworkPicMapper;

    public DisplayContentService(DisplayContentMapper displayContentMapper, UserMapper userMapper, ArtworkPicMapper artworkPicMapper) {
        this.displayContentMapper = displayContentMapper;
        this.userMapper = userMapper;
        this.artworkPicMapper = artworkPicMapper;
    }

    /**
     * 전시관람
     * @param display_idx 전시 고유 인덱스
     * @return ResponseEntity - List<DisplayContent>
     */
    public DefaultRes<List<DisplayContent>> findByDisplayIdx(final int display_idx){
        List<DisplayContent> dcList = displayContentMapper.findArtworksByDisplayIdx(display_idx);
        if(dcList == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
        for(DisplayContent displayContent : dcList){
            // displayContent.setU_name(userMapper.findUnameByUidx(displayContent.getU_idx())); -> 오류 떠서 잠시 주석 나중에 해결 부타캐요
            displayContent.setPic_url(artworkPicMapper.findByArtIdx(displayContent.getA_idx()).getPic_url());
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DISPLAY, dcList);
    }


    /**
     * 전시신청
     *
     * @param displayReq 전시 컨텐츠
     * @return ResponseEntity
     */
    @Transactional
    public DefaultRes save(final DisplayReq displayReq) {
        if(displayContentMapper.findByArtworkIdx(displayReq.getA_idx()) == null){
            try{
                displayContentMapper.save(displayReq);
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_DISPLAY);
            }catch(Exception e){
                log.info(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }else{
            //이미 전시에 등록한 경우
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_ALREADY_CREATE);
        }
    }


    /**
     * 전시신청 취소
     *
     * @param displayContent_idx 전시 컨텐츠 고유 인덱스
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes deleteDisplaycontent(final int displayContent_idx) {
        if( displayContentMapper.findByDisplayContentIdx(displayContent_idx) == null){
            try{
                displayContentMapper.delete(displayContent_idx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_DISPLAY);
            }catch(Exception e){
                log.info(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }else{
            //이미 전시에 등록한 경우
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_DELETE_DISPLAY);
        }

    }

}
