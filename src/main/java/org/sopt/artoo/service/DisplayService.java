package org.sopt.artoo.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Display;
import org.sopt.artoo.dto.DisplayContent;
import org.sopt.artoo.mapper.DisplayContentMapper;
import org.sopt.artoo.mapper.DisplayMapper;
import org.sopt.artoo.mapper.UserMapper;
import org.sopt.artoo.model.DateRes;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.DisplayAddReq;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class DisplayService {
    private DisplayMapper displayMapper;
    private DisplayContentMapper displayContentMapper;
    private UserMapper userMapper;
    private final S3FileUploadService s3FileUploadService;


    public DisplayService(DisplayMapper displayMapper, DisplayContentMapper displayContentMapper, UserMapper userMapper, S3FileUploadService s3FileUploadService) {
        this.displayMapper = displayMapper;
        this.displayContentMapper = displayContentMapper;
        this.userMapper = userMapper;
        this.s3FileUploadService = s3FileUploadService;

    }

    /**
     * 전시 메인 - 모든 전시 조회
     *
     * @return DefaultRes<List<Display>>
     */

    public DefaultRes<List<Display>> findDisplays(){
        List<Display> displayList = displayMapper.findAllDisplay();
        List<Display> nowDisplayList = new ArrayList<>();

        //v2
        for(Display display : displayList){
            if(DateRes.isContain(display.getD_sDateNow(), display.getD_eDateNow())) {nowDisplayList.add(display);}
        }
        for(Display nowDisplay: nowDisplayList){
            List<String> userList = new ArrayList<>();
            for(DisplayContent displayContent : displayContentMapper.findByDisplay(nowDisplay.getD_idx())){
                userList.add(userMapper.findByUidx(displayContent.getU_idx()).getU_name());
            }
            nowDisplay.setD_artworkUser(userList);
        }
        if(nowDisplayList.isEmpty())
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.FAIL_READ_DISPLAY, new ArrayList<>());
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_DISPLAY, nowDisplayList);
    }


    /**
     * 전시장 입장
     *
     * @param display_idx  전시장 고유 id
     * @return DefaultRes - <Display>
     */
    public DefaultRes<Display> findByDisplayIdx(final int display_idx){
        Display display = displayMapper.findByDisplayidx(display_idx);

        if(display == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.FAIL_READ_DISPLAY);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DISPLAY, display);
    }

    /**
     * 전시장 추가
     * @param displayAddReq
     * @return DefaultRes
     */
    public DefaultRes addDisplay(final DisplayAddReq displayAddReq){
        if(displayAddReq.checkProperties()){
            try {
                if(displayAddReq.getM_d_mainImg_url() != null){
                    displayAddReq.setD_mainImg_url(s3FileUploadService.upload(displayAddReq.getM_d_mainImg_url(),"display"));
                }

                if(displayAddReq.getM_d_titleImg_url() != null ){
                    displayAddReq.setD_titleImg_url(s3FileUploadService.upload(displayAddReq.getM_d_titleImg_url(),"display"));
                }
                displayAddReq.setD_repImg_url(s3FileUploadService.upload(displayAddReq.getM_d_repImg_url(), "display"));
                displayMapper.addDisplay(displayAddReq);

            } catch (IOException e) {
                e.printStackTrace();
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_CREATE_CONTENT);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.INSERT_DISPLAYS);
        }
        else {
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_REQUIRE_PARAMS);
        }
    }

    /**
     * 전시장 수정
     * @param displayAddReq
     * @return DefaultRes
     */
    public DefaultRes updateDisplay(final DisplayAddReq displayAddReq){
        if(displayAddReq.checkProperties()) {

            try {

                if (displayAddReq.getM_d_mainImg_url() != null) {
                    displayAddReq.setD_mainImg_url(s3FileUploadService.upload(displayAddReq.getM_d_mainImg_url(), "display"));
                }
                if (displayAddReq.getM_d_repImg_url() != null) {
                    displayAddReq.setD_repImg_url(s3FileUploadService.upload(displayAddReq.getM_d_repImg_url(), "display"));
                }
                if (displayAddReq.getM_d_mainImg_url() != null) {
                    displayAddReq.setD_titleImg_url(s3FileUploadService.upload(displayAddReq.getM_d_titleImg_url(), "display"));
                }

                displayMapper.updateDisplay(displayAddReq);

            } catch (IOException e) {
                e.printStackTrace();
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_UPDATE_CONTENT);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_DISPLAYS);

        }else{
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_REQUIRE_PARAMS);
        }
    }


    /**
     * 전시장 삭제
     * @param d_idx
     * @return DefaultRes
     */
    public DefaultRes deleteDisplay(final int d_idx){

        try {
            displayContentMapper.deleteByDIsplayIdx(d_idx);
            displayMapper.deleteByDisplayIdx(d_idx);
        } catch (Exception e) {
            e.printStackTrace();
        return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }

        return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_DISPLAYS);
    }
}
