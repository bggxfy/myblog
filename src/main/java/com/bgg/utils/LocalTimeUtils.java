package com.bgg.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class LocalTimeUtils {

    private static final String format = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime getDateTime(){
        SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
        String str = df.format(new Date());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(str, formatter);

    }

}
