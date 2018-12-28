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
            Calendar sCalApp = Calendar.getInstance();
            Calendar eCalApp = Calendar.getInstance();
            sCalApp.setTime(Date.valueOf(display.getD_sdateApply()));
            eCalApp.setTime(Date.valueOf(display.getD_edateApply()));

            Calendar sCalNow = Calendar.getInstance();
            Calendar eCalNow = Calendar.getInstance();
            sCalNow.setTime(Date.valueOf(display.getD_sdateNow()));
            eCalNow.setTime(Date.valueOf(display.getD_edateNow()));

            if(isContain(sCalApp, eCalApp)) display.setIsNow("0");
            if(isContain(eCalNow, eCalNow)) display.setIsNow("1");
        }

        if(displayList == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_DISPLAY);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_DISPLAY, displayList);
    }

    /**
     * 현재 시간이 sdate ~ edate 에 포함되는지 확인
     *
     * @return true - 포함
     * @return false - 불포함
     */
    public boolean isContain( final Calendar sdate, final Calendar edate){
        java.util.Date date = new java.util.Date();
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        log.info(now.toString());

        if(now.compareTo(sdate) != -1 && now.compareTo(edate)  != 1){
            return true;
        }else{
            return false;
        }
    }


    public String getMonth() {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        log.info(Integer.toString(month));
        return Integer.toString(month);
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
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_DISPLAY);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DISPLAY, display);
    }
}
