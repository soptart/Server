package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Purchase;
import org.sopt.artoo.dto.PurchaseDetail;
import org.sopt.artoo.dto.User;
import org.sopt.artoo.mapper.ArtworkMapper;
import org.sopt.artoo.mapper.PurchaseMapper;
import org.sopt.artoo.mapper.UserMapper;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.LoginReq;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminService {
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PurchaseMapper purchaseMapper;
    private final ArtworkMapper artworkMapper;

    public AdminService(JwtService jwtService, final UserMapper userMapper, final PurchaseMapper purchaseMapper, final ArtworkMapper artworkMapper) {
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.purchaseMapper = purchaseMapper;
        this.artworkMapper = artworkMapper;
    }

    /**
     * 관리자 아이디로만 로그인
     *
     * @param loginReq
     * @return loginToken
     */
    public DefaultRes adminLogin(final LoginReq loginReq) {
        final User adminUser = userMapper.findByIdAndPassword(loginReq);
        if (adminUser.getU_idx() == 0) {
            try {
                final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(adminUser.getU_idx()), adminUser.getU_idx());
                return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenDto);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                e.printStackTrace();
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.YOU_ARE_NOT_ADMIN);
    }

    /**
     * 모든 PurchaseList 반환
     *
     * @param
     * @return List<Purchase>
     */
    public DefaultRes<List<PurchaseDetail>> findAllUserPurchase() {
        try {
            List<Purchase> userPurchaseList = purchaseMapper.findAllPurchaseRecord();
            List<PurchaseDetail> result = new ArrayList<>();
            for (Purchase purchase : userPurchaseList) {
                PurchaseDetail purchaseDetail = new PurchaseDetail();
                purchaseDetail.setP_idx(purchase.getP_idx());
                purchaseDetail.setP_state(purchase.getP_state());
                purchaseDetail.setP_date(purchase.getP_date());
                purchaseDetail.setP_comment(purchase.getP_comment());
                purchaseDetail.setA_idx(purchase.getA_idx());
                purchaseDetail.setA_name(artworkMapper.findByIdx(purchase.getA_idx()).getA_name());
                purchaseDetail.setP_buyer_name(userMapper.findByUidx(purchase.getP_buyer_idx()).getU_name());
                purchaseDetail.setP_buyer_phone(userMapper.findByUidx(purchase.getP_buyer_idx()).getU_phone());
                purchaseDetail.setP_buyer_account(userMapper.findByUidx(purchase.getP_buyer_idx()).getU_bank() + ":" + userMapper.findByUidx(purchase.getP_buyer_idx()).getU_account());
                purchaseDetail.setP_seller_name(userMapper.findByUidx(purchase.getP_seller_idx()).getU_name());
                purchaseDetail.setP_seller_phone(userMapper.findByUidx(purchase.getP_seller_idx()).getU_phone());
                purchaseDetail.setP_seller_account(userMapper.findByUidx(purchase.getP_seller_idx()).getU_bank() + ":" + userMapper.findByUidx(purchase.getP_seller_idx()).getU_account());
                purchaseDetail.setP_recipient(purchase.getP_recipient());
                purchaseDetail.setP_address(purchase.getP_address());
                purchaseDetail.setP_price(purchase.getP_price());
                result.add(purchaseDetail);
            }
            if (userPurchaseList != null) {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_CONTENTS, result);
            }
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_CONTENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 아이디 검색 후 PurchaseList 반환
     *
     * @param
     * @return List<Purchase>
     */
    public DefaultRes<List<PurchaseDetail>> findUserPurchaseById(final String email) {
        final User user = userMapper.findByEmail(email);
        if (user != null) {
            try {
                List<Purchase> userPurchaseList = purchaseMapper.findTransactionByUserIdx(user.getU_idx());
                List<PurchaseDetail> result = new ArrayList<>();
                for (Purchase purchase : userPurchaseList) {
                    PurchaseDetail purchaseDetail = new PurchaseDetail();
                    purchaseDetail.setP_idx(purchase.getP_idx());
                    purchaseDetail.setP_state(purchase.getP_state());
                    purchaseDetail.setP_date(purchase.getP_date());
                    purchaseDetail.setP_comment(purchase.getP_comment());
                    purchaseDetail.setA_idx(purchase.getA_idx());
                    purchaseDetail.setA_name(artworkMapper.findByIdx(purchase.getA_idx()).getA_name());
                    purchaseDetail.setP_buyer_name(userMapper.findByUidx(purchase.getP_buyer_idx()).getU_name());
                    purchaseDetail.setP_buyer_phone(userMapper.findByUidx(purchase.getP_buyer_idx()).getU_phone());
                    purchaseDetail.setP_buyer_account(userMapper.findByUidx(purchase.getP_buyer_idx()).getU_bank() + ":" + userMapper.findByUidx(purchase.getP_buyer_idx()).getU_account());
                    purchaseDetail.setP_seller_name(userMapper.findByUidx(purchase.getP_seller_idx()).getU_name());
                    purchaseDetail.setP_seller_phone(userMapper.findByUidx(purchase.getP_seller_idx()).getU_phone());
                    purchaseDetail.setP_seller_account(userMapper.findByUidx(purchase.getP_seller_idx()).getU_bank() + ":" + userMapper.findByUidx(purchase.getP_seller_idx()).getU_account());
                    purchaseDetail.setP_recipient(purchase.getP_recipient());
                    purchaseDetail.setP_address(purchase.getP_address());
                    purchaseDetail.setP_price(purchase.getP_price());
                    result.add(purchaseDetail);
                }
                if (userPurchaseList != null) {
                    return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_CONTENTS, result);
                }
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_CONTENT);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
    }

    /**
     * 해당 거래 내역 조회
     *
     * @param p_idx
     * @return
     */
    public DefaultRes<Purchase> findPurchaseByPidx(final int p_idx) {
        final Purchase purchase = purchaseMapper.findPurchaseByPurchaseIdx(p_idx);
        try {
            if (purchase != null) {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, purchase);
            }
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_CONTENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 구매 상태 수정
     *
     * @param p_idx
     * @param p_state
     */
    public DefaultRes updatePurchaseState(final int p_idx, final int p_state) {
        log.info("p_idx is: "+String.valueOf(p_idx));
        final Purchase purchase = purchaseMapper.findPurchaseByPurchaseIdx(p_idx);
        try {
            if (purchase != null) {
                purchaseMapper.updatePurchaseState(p_idx, p_state);
                purchase.setP_state(p_state);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_PURCHASE, purchase);
            }
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_CONTENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }

    }
}
