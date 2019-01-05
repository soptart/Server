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
import org.sopt.artoo.utils.ResponseMessage;
import org.sopt.artoo.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        if(nowDisplayList == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.FAIL_READ_DISPLAY);
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

}
