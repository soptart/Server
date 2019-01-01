package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.dto.Purchase;
import org.sopt.artoo.dto.User;
import org.sopt.artoo.mapper.ArtworkMapper;
import org.sopt.artoo.mapper.PurchaseMapper;
import org.sopt.artoo.mapper.UserMapper;
import org.sopt.artoo.model.DateRes;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.DisplayContentRes;
import org.sopt.artoo.model.NoticeRes;
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

    public NoticeService(PurchaseMapper purchaseMapper, ArtworkMapper artworkMapper, UserMapper userMapper) {
        this.purchaseMapper = purchaseMapper;
        this.artworkMapper = artworkMapper;
        this.userMapper = userMapper;
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
     * @return DefaultRes - List<Display>
     */
//    public DefaultRes findDisplayApply(final int u_idx) {
//        DisplayContentRes displayContentRes =
//    }
}