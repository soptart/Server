package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.*;
import org.sopt.artoo.mapper.*;
import org.sopt.artoo.model.*;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.sopt.artoo.utils.constants.NoticeConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@Service
public class NoticeService {
    private PurchaseMapper purchaseMapper;
    private ArtworkMapper artworkMapper;
    private UserMapper userMapper;
    private DisplayContentMapper displayContentMapper;
    private DisplayMapper displayMapper;

    public NoticeService(PurchaseMapper purchaseMapper, ArtworkMapper artworkMapper, UserMapper userMapper, DisplayContentMapper displayContentMapper, DisplayMapper displayMapper) {
        this.purchaseMapper = purchaseMapper;
        this.artworkMapper = artworkMapper;
        this.userMapper = userMapper;
        this.displayContentMapper = displayContentMapper;
        this.displayMapper = displayMapper;
    }

    /**
     * 구매 내역 조회
     *
     * @param u_idx  유저 idx
     * @return DefaultRes - List<NoticeRes>
     */
    public DefaultRes findBuysByUidx(final int u_idx){
        try{
            List<NoticeRes> noticeResList  = new ArrayList<NoticeRes>();
            List<Purchase> purchaseList  = purchaseMapper.findByBuyerIdx(u_idx); //u_idx == 구매자

            for(Purchase purchase : purchaseList){
                // 거래 정보 저장
                NoticeRes noticeRes = new NoticeRes(purchase);
                noticeRes.setP_date(DateRes.getDate1(purchase.getP_date()));

                // 작품 정보 저장
                Artwork artwork = artworkMapper.findByIdx(purchase.getA_idx());
                noticeRes.setA_name(artwork.getA_name());
                noticeRes.setA_u_name(artwork.getA_name()); // 작가 == 판매자

                // 판매자 정보 저장
                User user = userMapper.findByUidx(purchase.getP_seller_idx());
                noticeRes.setU_name(user.getU_name());
                noticeRes.setU_idx(user.getU_idx());
                noticeRes.setU_address(user.getU_address());
                noticeRes.setU_phone(user.getU_phone());

                noticeResList.add(noticeRes);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BUYS, noticeResList);
        }catch(Exception e){
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }


    /**
     * 판매 내역 조회
     *
     * @param u_idx  유저 idx
     * @return DefaultRes - List<NoticeRes>
     */
    public DefaultRes findSellsByUidx(final int u_idx){
        try{
            List<NoticeRes> noticeResList  = new ArrayList<NoticeRes>();
            List<Purchase> purchaseList  = purchaseMapper.findBySellerIdx(u_idx); //u_idx == 판매자
            log.info("aa");
            for(Purchase purchase : purchaseList){
                NoticeRes noticeRes;
                // 거래 정보 저장
                try {
                    noticeRes = new NoticeRes(purchase);
                }catch(Exception e){
                    log.info(e.getMessage());
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
                }
                noticeRes.setP_date(DateRes.getDate1(purchase.getP_date()));

                // 작품 정보 저장
                Artwork artwork = artworkMapper.findByIdx(purchase.getA_idx());
                noticeRes.setA_name(artwork.getA_name());
                noticeRes.setA_u_name(userMapper.findByUidx(u_idx).getU_name());

                // 구매자 정보 저장
                User user = userMapper.findByUidx(purchase.getP_buyer_idx());
                noticeRes.setU_name(user.getU_name());
                noticeRes.setU_phone(user.getU_phone());
                noticeRes.setU_address(user.getU_address());

                noticeResList.add(noticeRes);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BUYS, noticeResList);

        }catch(Exception e){
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 구매 후기 작성
     * @param u_idx
     * @param p_idx
     * @param p_comment
     * @return
     */
    public DefaultRes trySavePurchaseComment(final int u_idx, final int p_idx, final String p_comment){
        try {
            Purchase purchase = purchaseMapper.findPurchaseByPurchaseIdx(p_idx);
            if (purchase.getP_buyer_idx()==u_idx){
                purchaseMapper.updatePurchaseComment(p_idx, p_comment);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_COMMENT);
            }else{
                return DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /*public DefaultRes refundPurchase(final int u_idx, final int p_idx){
        Purchase purchase = purchaseMapper.findPurchaseByPurchaseIdx(p_idx);
    }*/

    /**
     * 전시내역 조회
     *
     * @param u_idx  유저 idx
     * @return DefaultRes - List<DisplayRes>
     */
    public DefaultRes findNoticeDisplayApply(final int u_idx) {
        try {
            List<Display> displayList = displayMapper.findAllDisplay();
            List<DisplayContent> displayContents_apply = new ArrayList<>(); // 신청 중인 전시
            List<DisplayContent> displayContents =  new ArrayList<>(); // 전시 완료된 전시
            List<DisplayContent> displayContents_wait =  new ArrayList<>(); // 확정되어서 대기 중인 전시

            for(Display display : displayList){
                // 신청 중인 전시회 (신청 완료)
                if(DateRes.isContain(display.getD_sDateApply(), display.getD_eDateApply())){
                    displayContents_apply.add(displayContentMapper.findByUidxAndDidx(u_idx, display.getD_idx()));
                }// 전시 완료된 전시
                else if(DateRes.isCompareFromNow(display.getD_eDateNow())){
                    displayContents.add(displayContentMapper.findByUidxAndDidx(u_idx, display.getD_idx()));
                }// 확정되어서 대기 중인 전시
                else if(DateRes.isContain(display.getD_eDateApply(), display.getD_sDateNow())){
                    displayContents_wait.add(displayContentMapper.findByUidxAndDidx(u_idx, display.getD_idx()));
                }
            }

            try{
                // 신청 중인 전시 반환 리스트 생성
                List<DisplayRes> displayResList = new ArrayList<>();
                if(!displayContents_apply.isEmpty()) {insertRes(displayContents_apply, displayResList, NoticeConstant.displayContents_apply);}
                if(!displayContents.isEmpty()){ insertRes(displayContents, displayResList, NoticeConstant.displayContents);}
                if(!displayContents_wait.isEmpty()) {insertRes(displayContents_wait, displayResList, NoticeConstant.displayContents_wait);}

                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DISPLAY_APPLY, displayResList);
            }catch(Exception e) {
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, "으아아ㅏ");
            }

        }catch(Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    public  List<DisplayRes> insertRes(List<DisplayContent> displayContentList, List<DisplayRes> displayResList, int state){
        try {
            if (!displayContentList.isEmpty()) {
                for (DisplayContent displayContent : displayContentList) {
                    User user = userMapper.findByUidx(displayContent.getU_idx());
                    Artwork artwork = artworkMapper.findByIdx(displayContent.getA_idx());
                    Display display = displayMapper.findByDisplayidx(displayContent.getD_idx());

//                    log.info(artwork.getA_name());
//                    log.info(String.valueOf(artwork.getA_idx()));
//                    DisplayRes displayRes = new DisplayRes(display, artwork.getA_idx(), artwork.getA_name(), user.getU_idx(), user.getU_name(), state, DateRes.getDate1(date));
                    DisplayRes displayRes = new DisplayRes();
//                    log.info(displayContent.getDc_date().toString());

                    Date date = displayContent.getDc_date();
                    displayRes.setDc_date(DateRes.getDate1(date));
                    displayRes.setDisplay(display);
                    displayRes.setA_idx(artwork.getA_idx());
                    displayRes.setA_name(artwork.getA_name());
                    displayRes.setU_idx(user.getU_idx());
                    displayRes.setU_name(user.getU_name());
                    displayRes.setState(state);
                    displayRes.setDc_idx(displayContent.getDc_idx());

                    displayResList.add(displayRes);
                }
            }
            return displayResList;
        }catch(Exception e){
            log.error(e.getMessage());
            return displayResList;
        }
    }
}