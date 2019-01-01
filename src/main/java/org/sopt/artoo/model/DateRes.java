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
     * ex. yyyy.MM. ~ yyyy.MM
     *
     * 전시신청서 사용 date 포맷
     * @param
     * @return   yyyy.MM. ~ yyyy.MM
     */
    public static String getDate2( final String sdate, final String edate) throws Exception{
        SimpleDateFormat dt = new SimpleDateFormat("yyyy.MM");
        Date sDate = dt.parse(sdate);
        Date eDate = dt.parse(edate);
//        log.info(sDate.toString());
//        log.info(eDate.toString());
        return sDate.toString() + "~" + eDate.toString();
    }

}
