package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.*;
import org.sopt.artoo.mapper.*;
import org.sopt.artoo.model.*;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

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
     * 전시내역 조회
     *
     * @param u_idx  유저 idx
     * @return DefaultRes - List<DisplayRes>
     */
    public DefaultRes findNoticeDisplayApply(final int u_idx) {
        List<Display> displayList = displayMapper.findAllDisplay();
        List<Integer> displayIdxs = new ArrayList<Integer>();

        // 신청 중인 전시
        for(Display display : displayList){
            if(DateRes.isContain(display.getD_sDateApply(), display.getD_eDateApply())){ displayIdxs.add(display.getD_idx()); }
        }

        // user가 신청 중인 전시
        List<DisplayContent> displayContentList = new ArrayList<DisplayContent>();
        for(int d_idx: displayIdxs){
            displayContentList.add(displayContentMapper.findByUidxAndDidx(u_idx, d_idx));
        }

        // 신청 중인 전시 반환 리스트 생성
        List<DisplayRes> displayResList = new ArrayList<>();

        for(DisplayContent displayContent :  displayContentList){
            User user = userMapper.findByUidx(displayContent.getU_idx());
            Artwork artwork = artworkMapper.findByIdx(displayContent.getA_idx());
            Display display = displayMapper.findByDisplayidx(displayContent.getD_idx());

            DisplayRes displayRes = new DisplayRes(display, artwork.getA_idx(),artwork.getA_name(),user.getU_idx(), user.getU_name());
            displayResList.add(displayRes);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DISPLAY_APPLY, displayResList);
    }
}