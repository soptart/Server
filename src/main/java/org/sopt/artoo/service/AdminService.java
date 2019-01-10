package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Purchase;
import org.sopt.artoo.dto.User;
import org.sopt.artoo.mapper.PurchaseMapper;
import org.sopt.artoo.mapper.UserMapper;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.LoginReq;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
@Slf4j
public class AdminService {
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PurchaseMapper purchaseMapper;

    public AdminService(JwtService jwtService, final UserMapper userMapper, final PurchaseMapper purchaseMapper){
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.purchaseMapper = purchaseMapper;
    }

    /**
     * 관리자 아이디로만 로그인
     * @param loginReq
     * @return loginToken
     */
    public DefaultRes adminLogin(final LoginReq loginReq){
        final User adminUser = userMapper.findByIdAndPassword(loginReq);
        if(adminUser.getU_idx() == 0){
            try {
                final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(adminUser.getU_idx()), adminUser.getU_idx());
                return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenDto);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.YOU_ARE_NOT_ADMIN);
    }

    /**
     * 아이디 검색 후 PurchaseList 반환
     * @param loginReq
     * @return List<Purchase>
     */
    public DefaultRes<List<Purchase>> findUserPurchaseById(final LoginReq loginReq){
        final User user = userMapper.findByEmail(loginReq.getU_email());
        if(user != null) {
            try{
                List<Purchase> userPurchaseList = purchaseMapper.findTransactionByUserIdx(user.getU_idx());
                if(userPurchaseList != null){
                    return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_CONTENTS, userPurchaseList);
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
     * @param p_idx
     * @return
     */
    public DefaultRes<Purchase> findPurchaseByPidx(final int p_idx){
        final Purchase purchase = purchaseMapper.findPurchaseByPurchaseIdx(p_idx);
        try{
            if(purchase != null){
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, purchase);
            }
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_CONTENT);
        }catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 구매 상태 수정
     * @param p_idx
     * @param p_state
     */
    public DefaultRes updatePurchaseState(final int p_idx, final int p_state){
        final Purchase purchase = purchaseMapper.findPurchaseByPurchaseIdx(p_idx);
        try{
            if(purchase != null){
                purchaseMapper.updatePurchaseState(p_idx, p_state);
                DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_PURCHASE);
            }
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_CONTENT);
        }catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }

    }
}
