package org.sopt.artoo.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class DateRes {

    /**
     * 현재 month 조회
     *
     * @return int month
     */
    public static String getMonth() {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        log.info(Integer.toString(month));
        return Integer.toString(month);
    }

    /**
     * 현재 시간이 sdate ~ edate 에 포함되는지 확인
     *
     * @return true - 포함
     * @return false - 불포함
     */
    public static boolean isContain( final String sdate, final String edate) {
        try{
            log.info("isContain");
            java.util.Date date = new java.util.Date();

            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date sdate_date = dt.parse(sdate);
            java.util.Date edate_date = dt.parse(edate);

            if((date.getTime() >= sdate_date.getTime()) && (date.getTime() <= edate_date.getTime())){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * ex. 12.03(목)
     *
     * 알림 사용 date 포맷
     * @param
     * @return  12.03(목)
     */
    public static String getDate1(final Date date) throws Exception{
        SimpleDateFormat dt = new SimpleDateFormat("MM.dd(E)", Locale.KOREA);
        String parseDate = dt.format(date);
//        log.info(parseDate.toString());
        return parseDate;
    }
    /**
     * ex. 현재와 날짜 비교
     *
     *
     * @param
     * @return
     */
    public static Boolean isCompareFromNow( final String date){
        try{
            log.info("isCompareFromNow");
            java.util.Date now = new java.util.Date();
            log.info(now.toString());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parseDate = df.parse(date);

            // 현재 시간이 더 이후이면 true 리턴
            if((parseDate.getTime() <= now.getTime())){
                log.info("true");
                return true;
            }else{
                log.info("false");
                return false;
            }
        }catch(Exception e){
            log.info("exception");
            log.error(e.getMessage());
            return false;
        }

    }
}
