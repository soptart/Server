package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.ArtworkPic;
import org.sopt.artoo.dto.Home;
import org.sopt.artoo.dto.HomeData;
import org.sopt.artoo.dto.Tag;

import org.sopt.artoo.mapper.ArtworkPicMapper;
import org.sopt.artoo.mapper.HomeMapper;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class HomeService {

    private final HomeMapper homeMapper;
//    private final ArtworkMapper artworkMapper;
    private final ArtworkPicMapper artworkPicMapper;


    /**
     * HomeMapper 생성자 의존성 주입
     */
    public HomeService(HomeMapper homeMapper,  ArtworkPicMapper artworkPicMapper) {
        this.homeMapper = homeMapper;
//        this.artworkMapper = artworkMapper;
        this.artworkPicMapper = artworkPicMapper;
    }


//    public HomeService(HomeMapper homeMapper) {
//        this.homeMapper = homeMapper;
//    }


    /**
     * 좋아요 순위 5개 작가, 작가 작품
     * @return DefaultRes
     */
//    @Transactional
    public DefaultRes getAllTodayContents(){
        final List<Integer> todayUserIndex = homeMapper.findUserIdx(); //작가 u_idx 리스트
        List<Home> todayArtistList = new ArrayList<>();

        for(int i = 0; i < todayUserIndex.size(); i++){

            List<HomeData> artPicData = homeMapper.findArtistContentsByUserIdx(todayUserIndex.get(i)); //u_name, u_year 리스트, pic_url은 null
            for(HomeData artData : artPicData){
                artData.setPic_url(artworkPicMapper.findPicListByArtIdx(artData.getA_idx())); // artData의 pic_url 설정
            }
            Home todayArtist = new Home();
            todayArtist.setU_idx(todayUserIndex.get(i));
            todayArtist.setU_name(homeMapper.findArtistNameDescriptByUserIdx(todayUserIndex.get(i)).getU_name());
            todayArtist.setU_description(homeMapper.findArtistNameDescriptByUserIdx(todayUserIndex.get(i)).getU_description());
            todayArtist.setPic_Info(artPicData);

            todayArtistList.add(todayArtist);
        }


        if(todayArtistList.isEmpty()){
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTIST, todayArtistList);
    }


    /**
     *  테마 첫 화면 tag 테이블 넘겨주기
     * @return DefaultRes
     */
//    @Transactional
    public DefaultRes getAllTagInfo(){
        final List<Tag> themeList = homeMapper.findAllTag(); //모든 tag리스트 받아옴
        final Tag tag = themeList.get(0);
        int artwork_idx = homeMapper.findArtworkIdxByTagIdx(tag.getT_idx()); //tag가 갖고 있는 a_idx
        final List<ArtworkPic> themePicList = artworkPicMapper.findRecPicListByArtIdx(artwork_idx); //a_idx를 이용해서 0번째 picutre 6개
        tag.setFirstTag(themePicList);
        themeList.set(0, tag);

        if(themeList.isEmpty()){
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTIST, themeList);
    }

    @Transactional
    public DefaultRes<List<ArtworkPic>> getAllTagPicUrl(final int t_idx){
        int artwork_idx = homeMapper.findArtworkIdxByTagIdx(t_idx);
        final List<ArtworkPic> themePicList = artworkPicMapper.findPicListByArtIdx(artwork_idx);

        if(themePicList.isEmpty()){
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTIST, themePicList);
    }



}
