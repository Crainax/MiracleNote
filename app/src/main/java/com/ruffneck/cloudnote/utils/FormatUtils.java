package com.ruffneck.cloudnote.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A tool Utils used to format the data.
 */
public class FormatUtils {


    /**
     * A method is used to format the millis time to a string time like "mm:ss".
     * @param millis the time to format.
     * @return the formatted String.
     */
    public static String formatTime(long millis){

        long second = millis / 1000;

        long posSecond = second%60;
        long posMin = second/60;

        return (posMin >= 10 ? posMin : "0" + posMin) + ":" +(posSecond >= 10 ? posSecond : "0" + posSecond);
    }

    public static String formatSize(long size){
        DecimalFormat format = new DecimalFormat("0.00");

        return format.format((double)size/1024/1024)+"M";
    }

    public static String formatDate(Date date){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(date);
    }


}
