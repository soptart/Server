package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.dto.ArtworkLike;
import org.sopt.artoo.mapper.ArtworkMapper;
import org.sopt.artoo.mapper.ArtworkPicMapper;
import org.sopt.artoo.mapper.ArtworkLikeMapper;
import org.sopt.artoo.model.ArtworkReq;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ArtworkService {

    private final ArtworkMapper artworkMapper;
    private final ArtworkPicMapper artworkPicMapper;
    private final ArtworkLikeMapper artworkLikeMapper;
    private final S3FileUploadService s3FileUploadService;

    public ArtworkService(ArtworkMapper artworkMapper, ArtworkPicMapper artworkPicMapper, S3FileUploadService s3FileUploadService, ArtworkLikeMapper artworkLikeMapper) {
        this.artworkMapper = artworkMapper;
        this.artworkPicMapper = artworkPicMapper;
        this.s3FileUploadService = s3FileUploadService;
        this.artworkLikeMapper = artworkLikeMapper;
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
                Date date = new Date();
                artworkReq.setA_date(date);
                artworkMapper.save(artworkReq);
                final int artIdx = artworkReq.getA_idx();
                MultipartFile artwork = artworkReq.getPic_url();
                artworkPicMapper.save(artIdx, s3FileUploadService.upload(artwork));
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
        try {
            Artwork artwork = findByArtIdx(artworkReq.getA_idx()).getData();
            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_CONTENT, artwork);
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 컨텐츠 삭제
     *
     * @param artIdx 컨텐츠 고유 번호
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes deleteByArtIdx(final int artIdx) {
        try {
            artworkMapper.deleteByArtIdx(artIdx);
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_CONTENT);
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
        return userIdx == findByArtIdx(artIdx).getData().getU_idx();
    }

    public boolean checkLike(final int userIdx, final int artworkIdx) {
        return artworkLikeMapper.findByUserIdxAndArtworkIdx(userIdx, artworkIdx) != null;
    }

}
