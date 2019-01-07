package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.*;
import org.sopt.artoo.mapper.*;
import org.sopt.artoo.model.*;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.sopt.artoo.utils.constants.NoticeConstant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.sopt.artoo.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@Service
public class NoticeService {
    private PurchaseMapper purchaseMapper;
    private ArtworkMapper artworkMapper;
    private UserMapper userMapper;
    private DisplayContentMapper displayContentMapper;
    private DisplayMapper displayMapper;
    private ArtworkPicMapper artworkPicMapper;

    public NoticeService(PurchaseMapper purchaseMapper, ArtworkMapper artworkMapper, UserMapper userMapper, DisplayContentMapper displayContentMapper, DisplayMapper displayMapper, ArtworkPicMapper artworkPicMapper) {
        this.purchaseMapper = purchaseMapper;
        this.artworkMapper = artworkMapper;
        this.userMapper = userMapper;
        this.displayContentMapper = displayContentMapper;
        this.displayMapper = displayMapper;
        this.artworkPicMapper = artworkPicMapper;
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
            // 사용자가 구매자인 구매 목록 가져옴
            List<Purchase> purchaseList  = purchaseMapper.findByBuyerIdx(u_idx); //u_idx == 구매자

            for(Purchase purchase : purchaseList){

                NoticeRes noticeRes = new NoticeRes(purchase);
                noticeRes.setP_date(DateRes.getDate1(purchase.getP_date()));
                // 판매자 정보 저장
                User adminUser = userMapper.findByUidx(0);

                // 작품 정보 저장
                Artwork artwork = artworkMapper.findByIdx(purchase.getA_idx());
                noticeRes.setA_name(artwork.getA_name());
                noticeRes.setA_u_name(userMapper.findByUidx(artwork.getU_idx()).getU_name());

                noticeRes.setU_name(adminUser.getU_name());
                noticeRes.setU_phone(adminUser.getU_phone());
                noticeRes.setU_address(adminUser.getU_address());

                String p_state = String.valueOf(purchase.getP_state());

                if(p_state.endsWith("0")){ // 결제 전
                    noticeRes.setU_bank(adminUser.getU_bank());
                    noticeRes.setU_account(adminUser.getU_account());

                    noticeRes.setA_price(purchase.getP_price());
                    // 직거래 결제전
                    if(p_state.startsWith("1")){ noticeRes.setP_isDelivery(0); }
                    // 택배 결제전
                    else if(p_state.startsWith("2")){ noticeRes.setP_isDelivery(1); }
                    noticeRes.setA_pic_url(artworkPicMapper.findByArtIdx(artwork.getA_idx()).getPic_url());
                    noticeRes.setP_isPay(0); // 결제전
                    log.info(noticeRes.getA_idx() + ": 결제전");
                    noticeResList.add(noticeRes);
                }
                if(p_state.endsWith("1")){ //결제 완료
                    if(p_state.startsWith("1")){ // 직거래
                        noticeRes.setP_isDelivery(0);
                        log.info(noticeRes.getA_idx() + "직거래");
                    }else if(p_state.startsWith("2")){ // 택배
                        noticeRes.setP_isDelivery(1);
                        log.info(noticeRes.getA_idx() + "택배");
                    }
                    noticeRes.setA_pic_url(artworkPicMapper.findByArtIdx(artwork.getA_idx()).getPic_url());
                    noticeRes.setP_isPay(1); // 결제완료
                    noticeResList.add(noticeRes);
                }
            }
            if(noticeResList.isEmpty())
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_READ_BUYS, noticeResList);
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
            // 사용자가 판매자인 구매 목록 가져옴
            List<Purchase> purchaseList  = purchaseMapper.findBySellerIdx(u_idx); //u_idx == 판매자

            User adminUser = userMapper.findByUidx(0);

            for(Purchase purchase : purchaseList) {
                NoticeRes noticeRes = new NoticeRes(purchase);
                noticeRes.setP_date(DateRes.getDate1(purchase.getP_date()));

                // 작품 정보 저장
                Artwork artwork = artworkMapper.findByIdx(purchase.getA_idx());
                noticeRes.setA_name(artwork.getA_name());
                noticeRes.setA_u_name(userMapper.findByUidx(u_idx).getU_name()); // 작가 == user
                noticeRes.setA_pic_url(artworkPicMapper.findByArtIdx(artwork.getA_idx()).getPic_url());

                noticeRes.setU_name(adminUser.getU_name());
                noticeRes.setU_phone(adminUser.getU_phone());
                noticeRes.setU_address(adminUser.getU_address());

                int p_state = purchase.getP_state();
                // 직거래
                if (p_state == 11) { noticeRes.setP_isDelivery(0);  }
                // 택배
                else if (p_state == 21) { noticeRes.setP_isDelivery(1);}
                noticeResList.add(noticeRes);

            }
            if(noticeResList.isEmpty())
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_READ_SELLS, noticeResList);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_SELLS, noticeResList);

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

    /**
     * 환불 요청 상태 변경
     * @param p_idx
     * @return
     */
    public DefaultRes requestRefund(final int p_idx){
        try {
            Purchase purchase = purchaseMapper.findPurchaseByPurchaseIdx(p_idx);
            if(purchase.getP_state() == 10 || purchase.getP_state() == 20){ //purchase table
                purchaseMapper.deletePurchaseRow(p_idx);
            }
            else if (21 <= purchase.getP_state() && 23 >= purchase.getP_state()) {
                purchaseMapper.updatePurchaseState(p_idx, 30); //purchase 상태 수정
                Artwork artwork = artworkMapper.findByIdx(purchase.getA_idx());
                // artwork a_purchaseState (11|12|13 -> 1|2|3)
                artworkMapper.updatePurchaseStateByAIdx(artwork.getA_purchaseState() % 10, purchase.getA_idx());
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.REFUND_REQUEST_SUCCESS);
        }catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

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
//                else if(DateRes.isCompareFromNow(display.getD_eDateNow())){
//                    displayContents.add(displayContentMapper.findByUidxAndDidx(u_idx, display.getD_idx()));
//                }// 확정되어서 대기 중인 전시
//                else if(DateRes.isContain(display.getD_eDateApply(), display.getD_sDateNow())){
//                    displayContents_wait.add(displayContentMapper.findByUidxAndDidx(u_idx, display.getD_idx()));
//                }
            }
            // 신청 중인 전시 반환 리스트 생성
            List<DisplayRes> displayResList = new ArrayList<>();
            if(!displayContents_apply.isEmpty()) {insertRes(displayContents_apply, displayResList, NoticeConstant.displayContents_apply);}
//          if(!displayContents.isEmpty()){ insertRes(displayContents, displayResList, NoticeConstant.displayContents);}
//          if(!displayContents_wait.isEmpty()) {insertRes(displayContents_wait, displayResList, NoticeConstant.displayContents_wait);}

            if(!displayResList.isEmpty())
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DISPLAY_APPLY, displayResList);
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_DISPLAY_APPLY, displayResList);

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
                    DisplayRes displayRes = new DisplayRes();

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