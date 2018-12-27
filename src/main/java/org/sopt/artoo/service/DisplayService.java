package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Display;
import org.sopt.artoo.dto.DisplayContent;
import org.sopt.artoo.mapper.DisplayContentMapper;
import org.sopt.artoo.mapper.DisplayMapper;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.DisplayReq;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class DisplayService {
    private DisplayMapper displayMapper;
    private DisplayContentMapper displayContentMapper;
    private UserMapper userMapper;

    public DisplayService(DisplayMapper displayMapper, DisplayContentMapper displayContentMapper, UserMapper userMapper) {
        this.displayMapper = displayMapper;
        this.displayContentMapper = displayContentMapper;
        this.userMapper = userMapper;
    }

    /**
     * 모든 전시 조회
     *
     * @return DefaultRes<List<Display>>
     */
    public DefaultRes<List<Display>> findDisplays (){
        String month = getMonth();

        List<Display> displayList = displayMapper.findNow(month);
        List<Display> displayListApp = displayMapper.findApp(month);

        for(Display display : displayList) { display.setIsNow(1); }
        for(Display display : displayListApp) { display.setIsNow(0); }

        displayList.addAll(displayListApp);
        if(displayList == null || displayListApp == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, displayList);
    }

    public String getMonth() {
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        log.info(Integer.toString(month));
        return Integer.toString(month);
    }

    /**
     * 전시회 입장
     *
     * @param displayIdx 전시 고유 인덱스
     * @return DefaultRes
     */

    public DefaultRes<List<DisplayContent>> findByDisplayIdx(final int displayIdx){
        List<DisplayContent> dcList = displayMapper.findDisplayDetail(displayIdx);
        if(dcList == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

        for(DisplayContent displayContent : dcList){
            displayContent.setU_name(userMapper.findUnameByUidx(displayContent.getU_idx()));
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DISPLAY, dcList);
    }

    /**
     * 전시신청서
     *
     * @param displayReq 전시 신청 데이터
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes save(final DisplayReq displayReq) {
        if(displayContentMapper.findByArtworkIdx(displayReq.getA_idx()) == null){
            try{
                displayContentMapper.save(displayReq);
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_DISPLAY);
            }catch(Exception e){
                log.info(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }else{
            //이미 전시에 등록한 경우
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_ALREADY_CREATE);
        }
    }

    /**
     * 전시신청 취소
     *
     * @param dc_idx 전시 컨텐츠 idx
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes save(final DisplayReq displayReq) {
        if(displayContentMapper.findByArtworkIdx(displayReq.getA_idx()) == null){
            try{
                displayContentMapper.save(displayReq);
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_DISPLAY);
            }catch(Exception e){
                log.info(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }else{
            //이미 전시에 등록한 경우
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_ALREADY_CREATE);
        }
    }



}

