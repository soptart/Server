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

import java.util.ArrayList;
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
    public MyPageRes findUserWork(final int userIdx) {
        String u_name = userMapper.findByUidx(userIdx).getU_name();
        String userDes = findUserDescription(userIdx);
        List<Artwork> listArt = artworkMapper.findArtworkByUserIdx(userIdx);
        if(userMapper.findByUidx(userIdx) != null) {
            List<MyArtwork> myArtworks  = findMyArtWorklist(userIdx, listArt);
            if (!myArtworks.isEmpty()) {
                return MyPageRes.res(StatusCode.OK, ResponseMessage.READ_ALL_CONTENTS, u_name, userDes, myArtworks, myArtworks.size());
            }
            return MyPageRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
        }
        return  MyPageRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
    }

    public List<MyArtwork> findMyArtWorklist(final int userIdx, List<Artwork> listArt){ //주어진 Artwork List -> 작품 번호, 사진 번호, 전시 상태 찾기
        List<Display> listDisplay = displayMapper.findAllDisplay();
        List<Display> curDisplay = new LinkedList<>();
        List<Integer> curDisplayContentAidx = new LinkedList<>();

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
            //Artwork를 MyArtwork로 변환
            List<MyArtwork> listMyArtwork = new LinkedList<>();
            for (Artwork a : listArt) {
                MyArtwork myArtwork = new MyArtwork();
                myArtwork.setA_idx(a.getA_idx());
                myArtwork.setA_isDisplay(a.isA_isDisplay());
                if (artworkPicMapper.findByArtIdx(a.getA_idx()) != null) {
                    myArtwork.setA_url(artworkPicMapper.findByArtIdx(a.getA_idx()).getPic_url());
                } else {
                    myArtwork.setA_url(null);
                }
                listMyArtwork.add(myArtwork);
            }
            return listMyArtwork;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return null;
        }
    }



    /**
     * 유저가 클릭한 좋아요 조회
     *
     * @param userIdx 유저 인덱스
     * @return DefaultRes - List<ArtworkLike>
     */
    @Transactional
    public MyPageRes findUserLikes(final int userIdx) {
        List<ArtworkLike> listUserLike = artworkLikeMapper.findArtworkLikeByUserIdx(userIdx); //유저 ArtworkLike 객체 호출
        List<Artwork> listArtworks = new LinkedList<>();
        String u_name = userMapper.findByUidx(userIdx).getU_name();
        String userDes = findUserDescription(userIdx);
        for(ArtworkLike a : listUserLike){  // ArtworkLike -> artwork List로 변환
            Artwork artwork = artworkMapper.findByIdx(a.getA_idx());
            if(artwork != null) { // 비활성화 artwork 제외
                listArtworks.add(artwork);
            }
        }
        if (userMapper.findByUidx(userIdx) != null) {
            try {
                List<MyArtwork> myArtworks = findMyArtWorklist(userIdx, listArtworks); //userIdx와 artworkList로 Mypage에 해당하는 형식으로 변환
                if(!myArtworks.isEmpty()) {
                    return MyPageRes.res(StatusCode.CREATED, ResponseMessage.READ_USER_LIKES, u_name, userDes, myArtworks, myArtworks.size());
                }
                return MyPageRes.res(StatusCode.CREATED, ResponseMessage.NOT_FOUND_CONTENT);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return MyPageRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return MyPageRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
    }

    /**
     * 유저별 거래 내역 조회 (구매 + 판매)
     *
     * @param userIdx 유저 인덱스
     * @return DefaultRes - List<Purchase>
     */
    @Transactional
    public MyPageRes findUserPurchase(final int userIdx) {
        String userDes = findUserDescription(userIdx);
        String u_name = userMapper.findByUidx(userIdx).getU_name();
        if (userMapper.findByUidx(userIdx) == null) { // 회원 존재 유무
            return MyPageRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
        } else {
            try{
                List<Purchase> listPurchase = purchaseMapper.findTransactionByUserIdx(userIdx); //유저 고유 번호에서 거래 목록 불러오기
                if(!listPurchase.isEmpty()) {
                    ArrayList<UserPurchase> listTransaction = new ArrayList<>();
                    for (Purchase purchase : listPurchase) {
                        if (purchase.getP_buyer_idx() == userIdx) {
                            purchase.setP_isBuyer(true);
                        } else {
                            purchase.setP_isBuyer(false);
                        }
                        UserPurchase userPurchase = new UserPurchase();
                        userPurchase.setP_idx(purchase.getP_idx());
                        userPurchase.setA_idx(purchase.getA_idx());
                        userPurchase.setA_name(artworkMapper.findAllArtworkByIdx(purchase.getA_idx()).getA_name()); //조회 할때 비활성한 상품도 조회해야한다!
                        userPurchase.setBuyer(purchase.isP_isBuyer());
                        if (purchase.isP_isBuyer()) { //구매자일 경우
                            userPurchase.setU_name(userMapper.findByUidx(purchase.getP_buyer_idx()).getU_name());
                        } else {
                            userPurchase.setU_name(userMapper.findByUidx(purchase.getP_seller_idx()).getU_name());
                        }
                        userPurchase.setA_price(artworkMapper.findAllArtworkByIdx(purchase.getA_idx()).getA_price());
                        userPurchase.setP_state(purchase.getP_state());
                        userPurchase.setA_pic_url(artworkPicMapper.findByArtIdx(purchase.getA_idx()).getPic_url());
                        userPurchase.setP_date(purchase.getP_date().toString());
                        listTransaction.add(userPurchase);
                    }
                    return MyPageRes.res(StatusCode.OK, ResponseMessage.READ_ALL_TRANSACTION, u_name, userDes,
                            listTransaction, listTransaction.size());
                }
                return MyPageRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_CONTENT);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return MyPageRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
    }

    /**
     * 유저별 거래 후기 조회
     *
     * @param userIdx 유저 인덱스
     * @return DefaultRes - List<Purchase>
     */
    public MyPageRes findUserTransReview(final int userIdx) {
        List<Purchase> listTransaction = purchaseMapper.findTransactionByUserIdx(userIdx);
        List<UserReview> listFinishedTrans = new LinkedList<>();
        String userDes = findUserDescription(userIdx);
        String u_name = userMapper.findByUidx(userIdx).getU_name();
        if(userMapper.findByUidx(userIdx) != null) {
            try {
                for (Purchase p : listTransaction) {
                    if (p.getP_state() == 14 && p.isP_isBuyer() == false) { //10이 거래 완료라고 가정! 추후 수정 필요 && 판매자 여야함
                        UserReview userReview = new UserReview();
                        userReview.setP_idx(p.getP_idx());
                        userReview.setA_name(artworkMapper.findAllArtworkByIdx(p.getA_idx()).getA_name());
                        userReview.setU_name(userMapper.findByUidx(p.getP_buyer_idx()).getU_name());
                        userReview.setP_comment(p.getP_comment());
                        userReview.setP_date(p.getP_date().toString());
                        userReview.setA_pic_url(artworkPicMapper.findByArtIdx(p.getA_idx()).getPic_url());
                        listFinishedTrans.add(userReview);
                    }
                }
                if(!listFinishedTrans.isEmpty()) {
                    return MyPageRes.res(StatusCode.CREATED, ResponseMessage.READ_FINISHED_TRANSACTION, u_name, userDes,
                            listFinishedTrans, listFinishedTrans.size());
                }
                return MyPageRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return MyPageRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return MyPageRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
    }

    public String findUserDescription(final int userIdx) {
        final String userDescription = userMapper.findByUidx(userIdx).getU_description();
        return userDescription;
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
