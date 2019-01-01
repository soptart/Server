package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.dto.Display;
import org.sopt.artoo.dto.DisplayContent;
import org.sopt.artoo.mapper.DisplayContentMapper;
import org.sopt.artoo.mapper.DisplayMapper;
import org.sopt.artoo.model.DateRes;
import org.sopt.artoo.model.DefaultRes;
import org.sopt.artoo.model.DisplayReq;
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;


@Slf4j
@Service
public class DisplayService {
    private DisplayMapper displayMapper;

    public DisplayService(DisplayMapper displayMapper) {
        this.displayMapper = displayMapper;
    }

    /**
     * 전시 메인 - 모든 전시 조회
     *
     * @return DefaultRes<List<Display>>
     */

    public DefaultRes<List<Display>> findDisplays(){
        List<Display> displayList = displayMapper.findAllDisplay();

        //v1
//        for(Display display : displayList){
//            Calendar sCalApp = Calendar.getInstance();
//            Calendar eCalApp = Calendar.getInstance();
//            sCalApp.setTime(Date.valueOf(display.getD_sdateApply()));
//            eCalApp.setTime(Date.valueOf(display.getD_edateApply()));
//
//            Calendar sCalNow = Calendar.getInstance();
//            Calendar eCalNow = Calendar.getInstance();
//            sCalNow.setTime(Date.valueOf(display.getD_sdateNow()));
//            eCalNow.setTime(Date.valueOf(display.getD_edateNow()));
//
//            //신청 중
//            if(now.compareTo(sCalApp) != -1 && now.compareTo(eCalApp)  != 1){ display.setIsNow("0");}
//            //전시 중
//            if(now.compareTo(sCalNow) != -1 && now.compareTo(eCalNow)  != 1){ display.setIsNow("1"); }
//
//        }

        //v2
        for(Display display : displayList){
            if(DateRes.isContain(display.getD_sDateApply(), display.getD_eDateApply())) {display.setIsNow(0); }
            if(DateRes.isContain(display.getD_sDateNow(), display.getD_eDateNow())) {display.setIsNow(1);}
        }

        if(displayList == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.FAIL_READ_DISPLAY);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_DISPLAY, displayList);
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
}
