package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.dto.Display;
import org.sopt.artoo.dto.DisplayContent;
import org.sopt.artoo.dto.User;
import org.sopt.artoo.mapper.*;
import org.sopt.artoo.model.*;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DisplayContentService {
    private DisplayContentMapper displayContentMapper;
    private UserMapper userMapper;
    private ArtworkPicMapper artworkPicMapper;
    private ArtworkMapper artworkMapper;
    private DisplayMapper displayMapper;

    private DisplayService displayService;

    public DisplayContentService(DisplayContentMapper displayContentMapper, UserMapper userMapper, ArtworkPicMapper artworkPicMapper, ArtworkMapper artworkMapper, DisplayMapper displayMapper, DisplayService displayService) {
        this.displayContentMapper = displayContentMapper;
        this.userMapper = userMapper;
        this.artworkPicMapper = artworkPicMapper;
        this.artworkMapper = artworkMapper;
        this.displayMapper = displayMapper;
        this.displayService = displayService;
    }

    /**
     * 전시관람
     * @param d_idx 전시 고유 인덱스
     * @return DefaultRes - List<DisplayContentRes>
     */
    public DefaultRes<List<DisplayContentRes>> findByDisplayIdx(final int d_idx){
//         존재하지 않는 전시
        if(displayMapper.findByDisplayidx(d_idx) == null){ return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_DISPLAY); }

        // 전시 타이틀
        String d_title = displayMapper.findByDisplayidx(d_idx).getD_title();
        log.info(d_title);
        List<DisplayContentRes> dcList = displayContentMapper.findArtworksByDisplayIdx(d_idx);

//         전시회에 신청한 작품이 없을 경우
        if(dcList.isEmpty()){ return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_FOUND_DISPLAYCONTENT); }

        for(DisplayContentRes displayContent : dcList) {
            displayContent.setU_name(userMapper.findByUidx(displayContent.getU_idx()).getU_name());
            //작품 사진 없는 경우
            if(artworkPicMapper.findByArtIdx(displayContent.getA_idx()) != null)
                displayContent.setPic_url(artworkPicMapper.findByArtIdx(displayContent.getA_idx()).getPic_url());
            displayContent.setD_idx(d_idx);
            displayContent.setD_title(d_title);
        }

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DISPLAY, dcList);
    }

    /**
     * 전시신청서
     *
     * @return DefaultRes - List<DisplayApplyRes>
     */
    public DefaultRes findDisplayApply(final int u_idx){

        try{
            DisplayApplyRes displayApplyRes = new DisplayApplyRes();
            List<Artwork> artworks = artworkMapper.findArtworkByUserIdx(u_idx);

            // 유저 작품이 없을 경우-> null
            if(artworks.isEmpty()){ displayApplyRes.setArtworks(null); }
            else{ displayApplyRes.setArtworks(artworkMapper.findArtworkByUserIdx(u_idx));}

            List<Display> Alldisplays = displayMapper.findAllDisplay();
            List<Display> nowDisplay = new ArrayList<Display>();

            // 현재 신청 중인 전시만 저장
            for(Display display : Alldisplays){
                if(DateRes.isContain(display.getD_sDateApply(), display.getD_eDateApply())){
                    display.setIsNow(0);
                    nowDisplay.add(display);
                }
            }
            displayApplyRes.setDisplays(nowDisplay);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DISPLAY_APPLICATION, displayApplyRes);
        }catch(Exception e){
            log.info(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 전시신청
     *
     * @param displayReq 전시 컨텐츠
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes save(final DisplayReq displayReq) {
        if(displayReq.checkProperties()){
            // 이미 등록된 전시인지 확인
            if(displayContentMapper.findByUidxAndDidx(displayReq) == null){
                try{
                    displayContentMapper.save(displayReq);
                    Artwork a = artworkMapper.findByIdx(displayReq.getA_idx());
                    User u = userMapper.findByUidx(displayReq.getU_idx());
                    Display d = displayMapper.findByDisplayidx(displayReq.getD_idx());

                    DisplayApplyConfirmRes displayApplyConfirmRes = new DisplayApplyConfirmRes(
                            d.getD_idx(), d.getD_title(), d.getD_subTitle(),
                            u.getU_idx(), u.getU_name(), displayReq.getA_idx(), a.getA_name());
                    return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_DISPLAY, displayApplyConfirmRes);
                }catch(Exception e){
                    log.info(e.getMessage());
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
                }
            }else {
                //이미 전시에 등록한 경우
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_ALREADY_CREATE);
            }
        }
        // 요청 바디 부족
        else
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_CREATE_DISPLAY);
    }

//    /**
//     * 전시내역 - 알림
//     *
//     * @param user_idx user_idx
//     * @return DefaultRes
//     */
//    public DefaultRes findByUidx(final int user_idx) {
//        if(displayReq.checkProperties()){
//            // 이미 등록된 전시인지 확인
//            if(displayContentMapper.findByUidxAndDidx(displayReq) == null){
//                try{
//                    int idx = displayContentMapper.save(displayReq);
//                    return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_DISPLAY, idx);
//                }catch(Exception e){
//                    log.info(e.getMessage());
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//                }
//            }else {
//                //이미 전시에 등록한 경우
//                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_ALREADY_CREATE);
//            }
//        }
//        // 요청 바디 부족
//        else
//            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_CREATE_DISPLAY);
//    }

    /**
     * 전시신청 취소
     *
     * @param displayContent_idx 전시 컨텐츠 고유 인덱스
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes deleteDisplaycontent(final int displayContent_idx) {
        if( displayContentMapper.findByDisplayContentIdx(displayContent_idx) != null){
            try{
                displayContentMapper.deleteByDcIdx(displayContent_idx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_DISPLAY);
            }catch(Exception e){
                log.info(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }else{
            // 존재하지 않는 컨텐츠입니다.
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_DELETE_DISPLAY);
        }
    }
}



