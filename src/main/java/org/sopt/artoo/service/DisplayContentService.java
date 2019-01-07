package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.dto.Display;
import org.sopt.artoo.dto.User;
import org.sopt.artoo.mapper.*;
import org.sopt.artoo.model.*;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

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
     *
     * @param d_idx 전시 고유 인덱스
     * @return DefaultRes - List<DisplayContentRes>
     */
    public DefaultRes<List<DisplayContentRes>> findByDisplayIdx(final int d_idx){
//         존재하지 않는 전시
        if(displayMapper.findByDisplayidx(d_idx) == null){ return DefaultRes.res(StatusCode.FORBIDDEN, ResponseMessage.NOT_FOUND_DISPLAY, new ArrayList<>()); }

        // 전시 타이틀
        String d_title = displayMapper.findByDisplayidx(d_idx).getD_title();
        log.info(d_title);
        List<DisplayContentRes> dcList = displayContentMapper.findArtworksByDisplayIdx(d_idx);

        //전시회에 신청한 작품이 없을 경우
        if(dcList.isEmpty()){ return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_DISPLAYCONTENT, new ArrayList<>()); }

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

            // 유저 작품이 없을 경우-> []
            if(artworks.isEmpty()){ displayApplyRes.setArtworks(new ArrayList<>()); }
            else{
                for(Artwork artwork : artworks){
                    artwork.setPic_url(artworkPicMapper.findByArtIdx(artwork.getA_idx()).getPic_url());
                }
                displayApplyRes.setArtworks(artworks);
            }

            List<Display> Alldisplays = displayMapper.findAllDisplay();
            List<Display> nowDisplay = new ArrayList<Display>();

            if(!Alldisplays.isEmpty()) {
                // 현재 신청 중인 전시만 저장
                for (Display display : Alldisplays) {
                    if (DateRes.isContain(display.getD_sDateApply(), display.getD_eDateApply())) {
                        nowDisplay.add(display);
                    }
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
     * @return DefaultRes DisplayApplyConfirmRes
     */
    @Transactional
    public DefaultRes save(final DisplayReq displayReq) {
        if(displayReq.checkProperties()){
            // 이미 등록된 전시인지 확인
            if(displayContentMapper.findByUidxAndDidx(displayReq.getU_idx(), displayReq.getD_idx()) == null){
                try{
                    displayReq.setDc_date(DateRes.getDate());
                    User u = userMapper.findByUidx(displayReq.getU_idx());

                    if(artworkMapper.findByIdxAndUidx(displayReq.getA_idx(), displayReq.getU_idx())==null)
                        return DefaultRes.res(StatusCode.FORBIDDEN, ResponseMessage.NOT_FOUND_ARTWORK, new ArrayList<>());
                    Artwork a = artworkMapper.findByIdx(displayReq.getA_idx());

                    if(displayMapper.findByDisplayidx(displayReq.getD_idx())==null)
                        return DefaultRes.res(StatusCode.FORBIDDEN, ResponseMessage.NOT_FOUND_DISPLAY,  new ArrayList<>());
                    Display d = displayMapper.findByDisplayidx(displayReq.getD_idx());

                    displayContentMapper.save(displayReq);

                    try{
                        DisplayApplyConfirmRes displayApplyConfirmRes = new DisplayApplyConfirmRes();
                        displayApplyConfirmRes.setA_idx(a.getA_idx());
                        displayApplyConfirmRes.setA_name(a.getA_name());
                        displayApplyConfirmRes.setD_idx(d.getD_idx());
                        displayApplyConfirmRes.setD_subTitle(d.getD_subTitle());
                        displayApplyConfirmRes.setD_title( d.getD_title());
                        displayApplyConfirmRes.setDc_date(displayReq.getDc_date());
                        displayApplyConfirmRes.setU_idx(u.getU_idx());
                        displayApplyConfirmRes.setU_name(u.getU_name());
                        return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_DISPLAY, displayApplyConfirmRes);
                    }catch(Exception e){
                        log.info(e.getMessage());
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
                    }
                }catch(Exception e){
                    log.info(e.getMessage());
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
                }
            }else {
                //이미 전시에 등록한 경우
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.FAIL_ALREADY_CREATE,  new ArrayList<>());
            }
        }
        // 요청 바디 부족
        else
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.FAIL_CREATE_DISPLAY ,  new ArrayList<>());
    }


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
                return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_DISPLAY, displayContent_idx);
            }catch(Exception e){
                log.info(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }else{
            return DefaultRes.res(StatusCode.FORBIDDEN, ResponseMessage.NOT_FOUND_DISPLAYCONTENT);
        }
    }
}



