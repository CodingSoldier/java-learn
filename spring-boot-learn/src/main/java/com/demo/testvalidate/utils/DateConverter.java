package com.demo.testvalidate.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateConverter implements Converter<String, Date> {
    static final Logger logger = LoggerFactory.getLogger(DateConverter.class);

    @Override
    public Date convert(String source) {
        Date date = null;
        if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            date = parseDate(source, "yyyy-MM-dd");
        } else if (source.matches("^\\d{4}\\/\\d{1,2}\\/\\d{1,2}$")){
            date = parseDate(source, "yyyy/MM/dd");
        } else if (source.matches("[0-9]+")){  //时间戳格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = parseDate(sdf.format(Long.parseLong(source)), "yyyy-MM-dd HH:mm:ss");
        } else if(StringUtils.isEmpty(source) == false){
            logger.error("前台时间传参为："+source+"，后台不支持此时间格式。已将时间设置为null", new RuntimeException(""));
        }
        return date;
    }

    public  Date parseDate(String dateStr, String format) {
        Date date=null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        }catch (ParseException e){
            logger.error("时间转换出错", e);
        }
        return date;
    }
}

