package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.*;
import org.sopt.artoo.mapper.*;
import org.sopt.artoo.model.DateRes;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.UserSignUpReq;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.LinkedList;
import java.util.List;


@Slf4j
@Service
public class UserService {

    private final UserMapper userMapper;
    private final ArtworkMapper artworkMapper;
    private final PurchaseMapper purchaseMapper;
    private final ArtworkLikeMapper artworkLikeMapper;
    private final DisplayMapper displayMapper;
    private final DisplayContentMapper displayContentMapper;
    private final ArtworkPicMapper artworkPicMapper;




    public UserService(final UserMapper userMapper, final ArtworkMapper artworkMapper, final PurchaseMapper purchaseMapper,
                       final ArtworkLikeMapper artworkLikeMapper, final DisplayMapper displayMapper,
                       final DisplayContentMapper displayContentMapper, final ArtworkPicMapper artworkPicMapper) {
        this.userMapper = userMapper;
        this.artworkMapper = artworkMapper;
        this.purchaseMapper = purchaseMapper;
        this.artworkLikeMapper = artworkLikeMapper;
        this.displayMapper = displayMapper;
        this.displayContentMapper = displayContentMapper;
        this.artworkPicMapper = artworkPicMapper;
    }

    /**
     * 유저 객체 반환
     *
     * @param userIdx 유저 정보
     * @return DefaultRes - User 객체
     */
    public DefaultRes findUser(final int userIdx) {
        final User user = userMapper.findByUidx(userIdx);
        if (user != null) {
            try {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, user);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER);
    }

    /**
     * 회원 정보 저장
     *
     * @param userSignUpReq 회원 가입 정보
     * @return DefaultRes - 메시지
     */
    @Transactional
    public DefaultRes save(UserSignUpReq userSignUpReq) {
        if (userSignUpReq.checkQualification()) { //로그인 항목 필수 항목 검사
            final User user = userMapper.findByEmail(userSignUpReq.getU_email());
            if (user == null) { //이메일 중복 검사
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
     *
     * @param userIdx 유저 인덱스
     * @return DefaultRes - List<Artwork>
     */
    @Transactional
    public DefaultRes<List<MyArtwork>> findUserWork(final int userIdx) {
        List<Artwork> listArt = artworkMapper.findArtworkByUserIdx(userIdx);
        List<Display> listDisplay = displayMapper.findAllDisplay();
        List<Display> curDisplay = new LinkedList<>();
        List<Integer> curDisplayContentAidx = new LinkedList<>();

        if (!listArt.isEmpty()) {
            try {
                //전시 중인 display 찾기
                for (Display d : listDisplay) {
                    if (DateRes.isContain(d.getD_sDateNow(), d.getD_eDateNow())) {
                        curDisplay.add(d);
                    }
                }
                //전시 중인 display_content 작품 고유 번호 찾기
                for (Display d : curDisplay) {
                    for (DisplayContent dc : displayContentMapper.findDisplayContentByDisplay(d.getD_idx())) {
                        if (dc.getU_idx() == userIdx) {
                            curDisplayContentAidx.add(dc.getA_idx());
                        }
                    }
                }

                //display_content와 user의 작품 비교 -> 전시중인 작품 Mapping
                for (Integer i : curDisplayContentAidx) {
                    for (Artwork a : listArt) {
                        if (a.getA_idx() == i) {
                            a.setA_isDisplay(true);
                        }
                    }
                }

                List<MyArtwork> listMyArtwork = new LinkedList<>();

                for (Artwork A : listArt) {

                    MyArtwork myArtwork = new MyArtwork();
                    myArtwork.setA_idx(A.getA_idx());
                    myArtwork.setA_isDisplay(A.isA_isDisplay());
                    if(artworkPicMapper.findByArtIdx(A.getA_idx()) != null) {
                        myArtwork.setA_url(artworkPicMapper.findByArtIdx(A.getA_idx()).getPic_url());
                    }
                    else {
                        myArtwork.setA_url(null);
                    }
                    listMyArtwork.add(myArtwork);
                }

                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.READ_USER_ARTWORK, listMyArtwork);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
    }

    /**
     * 유저가 클릭한 좋아요 조회
     *
     * @param userIdx 유저 인덱스
     * @return DefaultRes - List<ArtworkLike>
     */
    public DefaultRes<List<ArtworkLike>> findUserLikes(final int userIdx) {
        List<ArtworkLike> listUserLike = artworkLikeMapper.findArtworkLikeByUserIdx(userIdx);
        if (userMapper.findByUidx(userIdx) != null) {
            try {
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.READ_USER_LIKES, listUserLike);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
    }

    /**
     * 유저별 거래 내역 조회 (구매 + 판매)
     *
     * @param userIdx 유저 인덱스
     * @return DefaultRes - List<Purchase>
     */
    public DefaultRes<List<Purchase>> findUserPurchase(final int userIdx) {
        if (userMapper.findByUidx(userIdx) == null) {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
        } else {
            List<Purchase> listTransaction = purchaseMapper.findTransactionByUserIdx(userIdx);
            for (Purchase purchase : listTransaction) {
                if (purchase.getP_buyer_idx() == userIdx) {
                    purchase.setP_isBuyer(true);
                } else {
                    purchase.setP_isBuyer(false);
                }
            }
            try {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_TRANSACTION, listTransaction);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        /*if(userMapper.findByUidx(userIdx) != null) {
            for(Purchase P : listTransaction){
                if(P.getU_idx() == userIdx){
                    P.setP_isBuyer(true);
                }
                else{
                    P.setP_isBuyer(false);
                }
            }
            try {
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.READ_ALL_TRANSACTION, listTransaction);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);*/
    }

    /**
     * 유저별 거래 후기 조회
     *
     * @param userIdx 유저 인덱스
     * @return DefaultRes - List<Purchase>
     */
    public DefaultRes<List<Purchase>> findUserTransReview(final int userIdx) {
        List<Purchase> listTransaction = purchaseMapper.findTransactionByUserIdx(userIdx);
        List<Purchase> listFinishedTrans = new LinkedList<>();
        for (Purchase P : listTransaction) {
            if (P.getP_state() == 10) { //10이 거래 완료라고 가정! 추후 수정 필요
                listFinishedTrans.add(P);
            }
        }
        try {
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.READ_FINISHED_TRANSACTION, listFinishedTrans);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 유저 자기 소개 반환
     *
     * @param userIdx 유저 인덱스
     * @return DefaultRes - List<Purchase>
     */
    public DefaultRes<String> findUserDescription(final int userIdx) {
        final String userDescription = userMapper.findByUidx(userIdx).getU_description();
        if (userMapper.findByUidx(userIdx) != null) {
            try {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, userDescription);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
    }

    /**
     * 유저 자기 소개 삽입/수정 후 반환
     *
     * @param userIdx 유저 인덱스 userDescription 유저 자기소개
     * @return DefaultRes - String
     */

    public DefaultRes<String> updateUserDescription(final int userIdx, final String userDescription) {
        try {
            userMapper.saveUserDescription(userIdx, userDescription);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_USER);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
