package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.dto.Purchase;
import org.sopt.artoo.dto.User;
import org.sopt.artoo.mapper.ArtworkMapper;
import org.sopt.artoo.mapper.PurchaseMapper;
import org.sopt.artoo.mapper.UserMapper;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.UserSignUpReq;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;


@Slf4j
@Service
public class UserService {

    private final UserMapper userMapper;
    private final ArtworkMapper artworkMapper;
    private final PurchaseMapper purchaseMapper;


    public UserService(final UserMapper userMapper, final ArtworkMapper artworkMapper, final PurchaseMapper purchaseMapper){
        this.userMapper = userMapper;
        this.artworkMapper = artworkMapper;
        this.purchaseMapper = purchaseMapper;
    }

    /**
     * 회원 정보 저장
     * @param userSignUpReq 회원 가입 정보
     * @return DefaultRes - 메시지
     */
    @Transactional
    public DefaultRes save(UserSignUpReq userSignUpReq) {
        if(userSignUpReq.checkQualification()) { //로그인 항목 필수 항목 검사
            final User user = userMapper.findByEmail(userSignUpReq.getU_email());
            if(user == null) { //이메일 중복 검사
                try {
                    userMapper.save(userSignUpReq);
                    return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER);
                } catch (Exception e) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    log.error(e.getMessage());
                    return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
                }
            } else return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.ALREADY_USER);
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_CREATE_USER);
    }

    /**
     * 유저별 작품 조회
     * @param userIdx 유저 인덱스
     * @return DefaultRes - List<Artwork>
     */
    public DefaultRes<List<Artwork>> findUserWork(final int userIdx) {
        List<Artwork> listArt = artworkMapper.findArtworkByUserIdx(userIdx);
        try{
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.READ_USER_ARTWORK, listArt);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 유저별 거래 내역 조회 (구매 + 판매)
     * @param userIdx 유저 인덱스
     * @return DefaultRes - List<Purchase>
     */
    public DefaultRes<List<Purchase>> findUserPurchase(final int userIdx){
        List<Purchase> listTransaction = purchaseMapper.findTransactionByUserIdx(userIdx);
        try{
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.READ_ALL_TRANSACTION, listTransaction);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }



}
