package com.yyyu.baselibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 功能：时间日期相关的工具类
 *
 * Created by yyyu on 2016/7/29.
 */
public class MyTimeUtils {

    /**
     * 忽略时分秒
     *
     * @param datetime
     * @return
     */
    public static long parseDateTimeToDate(String datetime){

        return parseDate("yyyy-MM-dd" ,datetime ).getTime();
    }


    /**
     * 得到当前小时
     *
     * @return
     */
    public static int getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 得到当前日期
     *
     * @return
     */
    public static long getCurrentDate(){

        String format = "yyyy-MM-dd";
        String dateStr = formatDateTime(format,new Date(System.currentTimeMillis()));

        return parseDate(format , dateStr).getTime();
    }

    /**
     * 得到当前的日期
     *
     * @return
     */
    public  static  String getCurrentDateTime(){

        return formatDateTime(new Date(System.currentTimeMillis()));
    }

    /**
     * 判断两个日期是否为同一天
     *
     * @param t1
     * @param t2
     * @return
     */
    public static boolean isSameDay(String t1,String t2){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal1=Calendar.getInstance();
        Calendar cal2=Calendar.getInstance();
        try {
            cal1.setTime(formatter.parse(t1));
            cal2.setTime(formatter.parse(t2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    /**
     * 两个时间比较
     *
     * @param t1
     * @param t2
     * @return
     */
    public static int timeCompare(String t1,String t2){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();
        try {
            c1.setTime(formatter.parse(t1));
            c2.setTime(formatter.parse(t2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result=c1.compareTo(c2);
        return result;
    }

    public static Date parseDate(String pattern , String str)  {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(pattern);
        TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Shanghai");
        DATE_FORMAT.setTimeZone(TIME_ZONE);
        try {
            return DATE_FORMAT.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate( String str){

        return parseDate("yyyy-MM-dd HH:mm:ss" , str);
    }

    public static String formatDateTime(Date date){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Shanghai");
        DATE_FORMAT.setTimeZone(TIME_ZONE);
        String dateStr = DATE_FORMAT.format(date);
        return dateStr;
    }

    public static String formatDateTime(String pattern , Date date){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(pattern);
        TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Shanghai");
        DATE_FORMAT.setTimeZone(TIME_ZONE);
        String dateStr = DATE_FORMAT.format(date);
        return dateStr;
    }

    public static String formatDateDT(long date) {
        Date currentdate = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(currentdate);
    }


    /**
     * 补全十位数（针对时、分）
     * @param i
     * @return
     */
    public static String complDigit(int i){
        String reslut = i+"";
        if(i<10){
            reslut = "0"+reslut;
        }
        return reslut;
    }

    /**
     * 根据年月得到当前月的天数
     * @param year
     * @param month
     * @return
     */
    public static int getDayNum(int year, int month) {
        int day ;
        boolean isLeap;
        switch (year % 4) {
            case 0:
                isLeap = true;
                break;
            default:
                isLeap = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = isLeap ? 28 : 29;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }


    /**
     * 得到当前星期几
     *
     * @return
     */
    public static  String getCurrentWeek() {

        return getWeek(System.currentTimeMillis());
    }

    /**
     * 得到传入时间的星期
     *
     * @param datetime
     * @return
     */
    public static String getWeek(long datetime){
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(datetime));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    /**
     * 得到当前日
     * @return
     */
    public static int getCurerntDay(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到当前月
     * @return
     */
    public static int getCurerntMonth(){
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    /**
     * 得到当前年
     * @return
     */
    public static int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

}
