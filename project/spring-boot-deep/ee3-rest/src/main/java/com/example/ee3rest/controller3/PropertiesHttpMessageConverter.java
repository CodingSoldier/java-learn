package com.example.ee3rest.controller3;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 自定义GenericHttpMessageConverter
 */
public class PropertiesHttpMessageConverter extends AbstractGenericHttpMessageConverter<Properties> {

    // 构造函数中配置支持的媒体类型
    public PropertiesHttpMessageConverter() {
        super(new MediaType("text", "properties"));
    }

    // controller方法返回值写入responseBody
    @Override
    protected void writeInternal(Properties properties, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        HttpHeaders httpHeaders = outputMessage.getHeaders();
        MediaType mediaType = httpHeaders.getContentType();
        Charset charset = mediaType.getCharset();
        charset = charset == null ? Charset.forName("UTF-8") : charset;
        OutputStream outputStream = outputMessage.getBody();
        Writer writer = new OutputStreamWriter(outputStream, charset);
        properties.store(writer, "From PropertiesHttpMessageConverter");
    }

    // 读取requestBody为Properties
    @Override
    protected Properties readInternal(Class<? extends Properties> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        // 获取请求头
        HttpHeaders httpHeaders = inputMessage.getHeaders();
        // 媒体类型
        MediaType mediaType = httpHeaders.getContentType();
        // 字符编码
        Charset charset = mediaType.getCharset();
        charset = charset == null ? Charset.forName("UTF-8") : charset;
        // requestBody
        InputStream inputStream = inputMessage.getBody();
        InputStreamReader reader = new InputStreamReader(inputStream, charset);
        // 为Properties对象加载requestBody流
        Properties properties = new Properties();
        properties.load(reader);
        return properties;
    }

    @Override
    public Properties read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return readInternal(null, inputMessage);
    }

}
