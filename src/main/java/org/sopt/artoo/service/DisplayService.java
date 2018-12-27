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

    public DisplayService(DisplayMapper displayMapper) {
        this.displayMapper = displayMapper;
    }

    /**
     * 전시 메인 - 모든 전시 조회
     *
     * @return DefaultRes<List<Display>>
     */

    public DefaultRes<List<Display>> findDisplays(){
        String month = getMonth();

        List<Display> displayList = displayMapper.findNow(month);
        List<Display> displayListApp = displayMapper.findApp(month);

        for(Display display : displayList) { display.setIsNow(1); }
        for(Display display : displayListApp) { display.setIsNow(0); }

        displayList.addAll(displayListApp);
        if(displayList == null || displayListApp == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_DISPLAY);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_DISPLAY, displayList);

//        java.util.Date date = new Date();
//        Calendar now = Calendar.getInstance();
//
//        List<Display> displayList = displayMapper.findAllDisplay();
//
//        for(Display display : displayList){
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(display.getD_sdateApp());
//
//            if(display.getD_sdateApp().compareTo(now)
//            display.getD_sdateApp()
//        }
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
     * 전시장 입장
     *
     * @param display_idx  전시장 고유 id
     * @return ResponseEntity - <Display>
     */
    public DefaultRes<Display> findByDisplayIdx(final int display_idx){
        Display display = displayMapper.findByDisplayidx(display_idx);

        if(display == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_DISPLAY);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DISPLAY, display);
    }

}

