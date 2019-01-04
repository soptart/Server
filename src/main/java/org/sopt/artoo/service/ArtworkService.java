package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.*;
import org.sopt.artoo.mapper.*;
import org.sopt.artoo.model.ArtworkFilterReq;
import org.sopt.artoo.mapper.ArtworkMapper;
import org.sopt.artoo.mapper.ArtworkPicMapper;
import org.sopt.artoo.model.ArtworkReq;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.PurchaseReq;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ArtworkService {

    private final ArtworkMapper artworkMapper;
    private final ArtworkPicMapper artworkPicMapper;
    private final UserMapper userMapper;
    private final ArtworkLikeMapper artworkLikeMapper;
    private final S3FileUploadService s3FileUploadService;
    private final PurchaseMapper purchaseMapper;
    private final CommentMapper commentMapper;
    private final DisplayContentMapper displayContentMapper;
    private final DisplayMapper displayMapper;

    public ArtworkService(ArtworkMapper artworkMapper, ArtworkPicMapper artworkPicMapper, UserMapper userMapper, ArtworkLikeMapper artworkLikeMapper, S3FileUploadService s3FileUploadService, PurchaseMapper purchaseMapper, CommentMapper commentMapper, DisplayContentMapper displayContentMapper, DisplayMapper displayMapper) {
        this.artworkMapper = artworkMapper;
        this.artworkPicMapper = artworkPicMapper;
        this.userMapper = userMapper;
        this.artworkLikeMapper = artworkLikeMapper;
        this.s3FileUploadService = s3FileUploadService;
        this.purchaseMapper = purchaseMapper;
        this.commentMapper = commentMapper;
        this.displayContentMapper = displayContentMapper;
        this.displayMapper = displayMapper;
    }

    /**
     * 모든 작품 조회
     *
     * @return DefaultRes
     */
    public DefaultRes<List<Artwork>> findAll() {
        List<Artwork> artworkList = artworkMapper.findAll();
        for (Artwork artwork : artworkList) {
            artwork.setPic_url(artworkPicMapper.findByArtIdx(artwork.getA_idx()));
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_CONTENTS, artworkList);
    }

    /**
     * 작품 인덱스로 조회
     *
     * @param a_idx 작품 인덱스
     * @return DefaultRes
     */
    public DefaultRes<Artwork> findByArtIdx(final int a_idx) {
        Artwork artwork = artworkMapper.findByIdx(a_idx);
        if (artwork == null) {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
        }
        artwork.setPic_url(artworkPicMapper.findByArtIdx(artwork.getA_idx()));
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, artwork);
    }

    /**
     * 작품 인덱스로 artworklist 조회
     *
     * @param a_idx 작품 인덱스
     * @return DefaultRes
     */
    public DefaultRes<List<ArtworkLike>> findAllLikesByArtIdx(final int a_idx) {
        List<ArtworkLike> likeList = artworkLikeMapper.findArtworkLikeByArtIdx(a_idx);
        if (likeList == null) {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NO_ARTWORKLIKE);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_ARTWORKLIKE, likeList);
    }

    public DefaultRes<Integer> getLikecountByArtIdx(final int a_idx) {
        Artwork artwork = artworkMapper.findByIdx(a_idx);
        if(artwork == null){
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, artwork.getA_like_count());
    }

    @Transactional
    public DefaultRes saveArtworkLike(final int a_idx, final int u_idx) {
        Artwork artwork = findByArtIdx(a_idx).getData();
        try {
            if (artwork == null) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
            }
            ArtworkLike artworkLike = artworkLikeMapper.findByUserIdxAndArtworkIdx(u_idx, a_idx);
            if (artworkLike == null) {
                artworkMapper.like(a_idx, artwork.getA_like_count() + 1);
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                artworkLikeMapper.save(u_idx, a_idx, sdf.format(date));
            } else {
                artworkMapper.like(a_idx, artwork.getA_like_count() - 1);
                artworkLikeMapper.deleteByUserIdxAndArtworkIdx(u_idx, a_idx);
            }
            artwork = findByArtIdx(a_idx).getData();
            artwork.setAuth(checkAuth(u_idx, a_idx));
            artwork.setIslike(checkLike(u_idx, a_idx));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.LIKE_CONTENT, artwork);
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 작품 저장
     *
     * @param artworkReq 작품 데이터
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes save(final ArtworkReq artworkReq) {
        if (artworkReq.checkProperties()) {
            try {
                log.info("artwork url: "+artworkReq.getPic_url().toString());
                if(artworkReq.getPic_url()==null){
                    return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.ARTWORK_NOPICUTRE);
                }
                Date date = new Date();
                artworkReq.setA_date(date);
                artworkMapper.save(artworkReq);

                final int artIdx = artworkReq.getA_idx();
                artworkPicMapper.save(artIdx, s3FileUploadService.upload(artworkReq.getPic_url()));
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_CONTENT);

            } catch (IOException e) {
                log.info(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_CREATE_CONTENT);
    }

    /**
     * 작품 데이터 수정
     *
     * @param artworkReq 작품 데이터
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes update(final ArtworkReq artworkReq) {
        if (artworkReq.checkProperties()) {
            try {
                log.info("update artwork url: "+artworkReq.getPic_url().toString());
                log.info("file name: "+artworkReq.getPic_url().getOriginalFilename());
                if(artworkReq.getPic_url()==null){
                    return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.ARTWORK_NOPICUTRE);
                }
                Date date = new Date();
                artworkReq.setA_date(date);
                artworkReq.setA_active(true);
                artworkPicMapper.update(artworkReq.getA_idx(), s3FileUploadService.upload(artworkReq.getPic_url()));
                artworkMapper.updateByArtIdxReq(artworkReq, artworkReq.getA_idx());
                return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_CONTENT, artworkReq);
            } catch (Exception e) {
                log.error(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_UPDATE_CONTENT);
    }

    /**
     * 컨텐츠 삭제
     *
     * @param artIdx
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes deleteByArtIdx(final int artIdx) {
        try {
            if(purchaseMapper.findTransactionsByArtIdx(artIdx).isEmpty()){
                artworkLikeMapper.deleteByArtIdx(artIdx);
                artworkPicMapper.deleteByArtIdx(artIdx);
                commentMapper.deleteByArtIdx(artIdx);
                DisplayContent displayContent = displayContentMapper.findByArtworkIdx(artIdx);
                if(displayContent!=null){
                    displayMapper.deleteByDisplayIdx(displayContent.getD_idx());
                }
                displayContentMapper.deleteByArtIdx(artIdx);
                artworkMapper.deleteByArtIdx(artIdx);
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_CONTENT);
            }else{
                Artwork artwork = artworkMapper.findByIdx(artIdx);
                artwork.setA_active(false);
                artworkMapper.updateByArtIdx(artwork, artIdx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.UNCOMPLETED_PURCHASE);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 글 권한 확인
     *
     * @param userIdx 사용자 고유 번호
     * @param artIdx  글 고유 번호
     * @return boolean
     */
    public boolean checkAuth(final int userIdx, final int artIdx) {
        return userIdx == artworkMapper.findByIdx(artIdx).getU_idx();
    }

    /**
     * 구매 Service - 구매 데이터 전달 + 가격 정보 올리기
     *
     * @param buyerIdx 구매자 고유번호
     * @param a_idx 작품 고유번호
     * @param purchaseReq 구매 정보
     * @return boolean
     */
     @Transactional
     public DefaultRes<PurchaseProduct> purchaseArtwork(final int buyerIdx, final int a_idx, final PurchaseReq purchaseReq) {
        if(purchaseReq.checkPurchaseReq()) {
            try {
                //---------------------작품 데이터 전송--------------------
                //작품 정보
                PurchaseProduct purchaseProduct = new PurchaseProduct();
                final Artwork purchaseArtwork = artworkMapper.findByIdx(a_idx);
                final User artist = userMapper.findByUidx(purchaseArtwork.getU_idx());
                purchaseProduct.setArtworkName(purchaseArtwork.getA_name()); //작품 이름
                purchaseProduct.setArtworkPrice(purchaseArtwork.getA_price()); // 작품 가격
                purchaseProduct.setArtistName(artist.getU_name()); //작가 이름
                purchaseProduct.setArtistSchool(artist.getU_school()); // 작가 학교
                final int productSize = purchaseArtwork.getA_size();
                if(purchaseArtwork.getA_price() >= 150000){
                    purchaseProduct.setDeliveryCharge(0);
                }
                else if(productSize < 2412) {
                    purchaseProduct.setDeliveryCharge(3000);
                }
                else if(productSize < 6609){
                    purchaseProduct.setDeliveryCharge(4000);
                }
                else {
                    purchaseProduct.setDeliveryCharge(5000);
                }
                //---------------------구매 데이터 저장--------------------

                if(purchaseReq.isP_isPost()) { //상태
                    purchaseReq.setP_state(1);
                }
                else{
                    purchaseReq.setP_state(11);
                }
                Calendar calendar = Calendar.getInstance(); // 시간
                java.util.Date date = calendar.getTime();
                purchaseReq.setP_currentTime(date);

                purchaseReq.setA_idx(a_idx);
                purchaseReq.setP_sellerIdx(artist.getU_idx());
                purchaseReq.setP_buyerIdx(buyerIdx);

                purchaseMapper.savePurchaseData(purchaseReq);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_PURCHASE, purchaseProduct);
            } catch (Exception e) {
                log.error(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_PURCHASE);
     }

    public boolean checkLike(final int userIdx, final int artworkIdx) {
        return artworkLikeMapper.findByUserIdxAndArtworkIdx(userIdx, artworkIdx) != null;
    }



    /**
     * size, form, category를 이용하여 작품 필터
     * @param artworkFilterReq
     * @return Artwork
     */
    @Transactional
    public DefaultRes filterArtworkPic(final ArtworkFilterReq artworkFilterReq){
        try{
            List<ArtworkPic> artworkPicList = new ArrayList<>();
            List<Integer> artworkIdxList = new ArrayList<>();

            String size = artworkFilterReq.getA_size();
            String form = artworkFilterReq.getA_form();
            String category = artworkFilterReq.getA_category();

            if(size != null){
                switch (size) {
                    case "S":
                        artworkIdxList = artworkMapper.findArtIdxBySize(0, 2411); //사이즈가 S인 a_idx List

                        break;
                    case "M":
                        artworkIdxList = artworkMapper.findArtIdxBySize(2412, 6608); //사이즈가 M인 a_idx List

                        break;
                    case "L":
                        artworkIdxList = artworkMapper.findArtIdxBySize(6609, 10628); //사이즈가 L인 a_idx List

                        break;
                    case "XL":
                        artworkIdxList = artworkMapper.findArtIdxBySize(10629, 21134); //사이즈가 XL인 a_idx List

                        break;
                }
            }
            if(form != null){
                List<Integer> artworkIdxByFormList = artworkMapper.findArtIdxByForm(form);
                if(artworkIdxList.size() != 0) {
                    artworkIdxList.retainAll(artworkIdxByFormList); //artworkIdxList에서 artworkIdxByFormList와  공통 요소만 저장 공통 요소만 저장

                }
            }
            if(category != null){
                List<Integer> artworkIdxByCategoryList = artworkMapper.findArtIdxByCategory(category);
                if(artworkIdxList.size() != 0){
                    artworkIdxList.retainAll(artworkIdxByCategoryList);//artworkIdxList에서 artworkIdxByCategoryList와 공통 요소만 저장
                }
            }
            if(size == null && form == null && category == null){
                artworkPicList = artworkPicMapper.findAllArtworkPic();
            }
            for(int a_idx : artworkIdxList){
                artworkPicList.add(artworkPicMapper.findByArtIdx(a_idx));
            }

            if(artworkPicList.isEmpty()){
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_CONTENTS, artworkPicList);

        }catch (Exception e){
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
