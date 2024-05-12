package com.hellobike.pmo.cockpit.util;

import org.springframework.format.datetime.joda.DateTimeParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static Date parseDate(String dateString){


        // 将字符串解析为 Instant 对象
        Instant instant = Instant.parse(dateString);

        // 将 Instant 对象转换为中国时区的 ZonedDateTime 对象
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Shanghai"));

        // 将 ZonedDateTime 对象转换为 java.util.Date 对象
        Date chinaDate = Date.from(zonedDateTime.toInstant());
        return chinaDate;
    }
    public static String parseDate2String(String dateString) {
        try {
            // 创建一个 SimpleDateFormat 对象，其模式与 dateString 的格式相匹配
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

            // 将 dateString 解析为 Date 对象
            Date date = sdf.parse(dateString);

            // 将 Date 对象转换为 Instant 对象
            Instant instant = date.toInstant();

            // 将 Instant 对象转换为中国时区的 ZonedDateTime 对象
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Shanghai"));

            // 创建一个 DateTimeFormatter 对象，用于将 ZonedDateTime 对象转换为字符串
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // 将 ZonedDateTime 对象转换为字符串
            String chinaDateString = formatter.format(zonedDateTime);

            return chinaDateString;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String dateString = "2023-12-31T16:00:00.000Z";

        System.out.println(parseDate(dateString));
    }
}
