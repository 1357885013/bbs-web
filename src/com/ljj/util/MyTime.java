package com.ljj.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyTime {
    public static String formateTime(long time) {

        Calendar calNow = Calendar.getInstance();
        calNow.setTime(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));

        SimpleDateFormat format = new SimpleDateFormat();
        long timeDiff = calNow.getTimeInMillis() - cal.getTimeInMillis();
        if (timeDiff < 60000) {
            return timeDiff / 1000 + "秒前";
        } else if (timeDiff < 3600000) {
            //同一小时，几分钟前
            return timeDiff / 60000 + "分钟前";
        } else if (timeDiff < 86400000) {
            //同一天，一天86400秒  今天几点几分   几分钟前
            format.applyPattern("今天HH点mm分  "+timeDiff/60000+"分钟前");
            return format.format(cal.getTime());
        } else if (calNow.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && calNow.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
            //同一月，28天2419200   这个月几号 几点几分  几天前
            long temp = timeDiff/6000*60*24;
            format.applyPattern("本月dd号 HH点mm分  "+temp+"天前");
            return format.format(cal.getTime());
        } else if (calNow.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
            //同一年，一年31622400秒  今年 几月几号几点
            format.applyPattern("MM月dd号 HH点mm分");
            return format.format(cal.getTime());
        } else {
            //至少一年前
            format.applyPattern("yy年MM月dd号 HH点");
            return format.format(cal.getTime());
        }

    }
}
