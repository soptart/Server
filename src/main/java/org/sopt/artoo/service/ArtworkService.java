package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.dto.ArtworkPic;
import org.sopt.artoo.mapper.ArtworkMapper;
import org.sopt.artoo.mapper.ArtworkPicMapper;
import org.sopt.artoo.model.ArtworkFilterReq;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ArtworkService {

    private final ArtworkMapper artworkMapper;
    private final ArtworkPicMapper artworkPicMapper;
    private final S3FileUploadService s3FileUploadService;

    public ArtworkService(ArtworkMapper artworkMapper, ArtworkPicMapper artworkPicMapper, S3FileUploadService s3FileUploadService) {
        this.artworkMapper = artworkMapper;
        this.artworkPicMapper = artworkPicMapper;
        this.s3FileUploadService = s3FileUploadService;
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
     * @param artIdx 글 고유 번호
     * @return boolean
     */
    public boolean checkAuth(final int userIdx, final int artIdx){
        return userIdx == findByArtIdx(artIdx).getData().getU_idx();
    }


    /**
     * 필터
     *
     */
    @Transactional
    public DefaultRes filterArtworkPic(final ArtworkFilterReq artworkFilterReq){
        try{
            List<ArtworkPic> artworkPicList = new ArrayList<>();
            List<Integer> artworkIdxList = new ArrayList<>();

            String size = artworkFilterReq.getSize();
            String form = artworkFilterReq.getForm();
            String category = artworkFilterReq.getCategory();

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
