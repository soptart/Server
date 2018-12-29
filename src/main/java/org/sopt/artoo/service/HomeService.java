package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.ArtworkPic;
import org.sopt.artoo.dto.Home;
import org.sopt.artoo.dto.Tag;

import org.sopt.artoo.mapper.ArtworkPicMapper;
import org.sopt.artoo.mapper.HomeMapper;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        final List<Integer> todayUserIndex = homeMapper.findUserIdx(); //작가 u_idx 리스트 받아옴
        List<Home> todayArtistList = null;//작가 u_name 받아오기

        for(int i = 0; i<todayUserIndex.size(); i++){
            todayArtistList.set(i, homeMapper.findTodayContents(todayUserIndex.get(0))); // u_name, a_name, a_year, a_idx 갖고옴
            todayArtistList.get(i).setPic_url(artworkPicMapper.findPicListByArtIdx(todayArtistList.get(i).getA_idx()));
//        for(Home todayArtist : todayUserIndex){
//            todayArtist.setA_name(homeMapper.findTodayContents(todayArtist.getU_idx())); //user_idx 넘겨주면 artwork name갖고 오기
//            todayArtist.setU_name(homeMapper.findTodayContents(todayArtist.getU_idx())); //user_idx 넘겨주면 user name 갖고오기
//            todayArtist.setA_year(homeMapper.findTodayContents(todayArtist.getU_idx()));
//            todayArtist.setA_idx(homeMapper.findTodayContents(todayArtist.getU_idx()));
//            todayArtist.setPic_url(artworkPicMapper.findPicListByArtIdx());
//            todayArtist.setPic_url(); //user_idx artworkPicMapper 사용해서 List 갖고오기
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
