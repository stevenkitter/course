package com.julu666.course.api.utils;

import java.util.Calendar;
import java.util.Date;

public class DateOperation {

    public static Date getDateAfter(Date d, int day){
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        return now.getTime();
    }

}
