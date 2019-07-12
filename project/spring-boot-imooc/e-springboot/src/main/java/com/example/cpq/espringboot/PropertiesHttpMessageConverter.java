package com.example.cpq.espringboot;

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
 * @Description
 * @Author chenpiqian
 * @Date: 2019-07-12
 */
public class PropertiesHttpMessageConverter extends AbstractGenericHttpMessageConverter<Properties> {

    public PropertiesHttpMessageConverter() {
        // 设置支持的 MediaType
        super(new MediaType("text", "properties"));
    }

    // 反序列化响应结果
    @Override
    protected void writeInternal(Properties properties, Type type, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        HttpHeaders httpHeaders = httpOutputMessage.getHeaders();
        MediaType mediaType = httpHeaders.getContentType();
        // 获取字符编码
        Charset charset = mediaType.getCharset();
        // 当 charset 不存在时，使用 UTF-8
        charset = charset == null ? Charset.forName("UTF-8") : charset;
        // 字节输出流
        OutputStream outputStream = httpOutputMessage.getBody();
        // 字符输出流
        Writer writer = new OutputStreamWriter(outputStream, charset);
        // Properties 写入到字符输出流
        properties.store(writer,"From PropertiesHttpMessageConverter");
    }

    // 序列化请求参数
    @Override
    protected Properties readInternal(Class<? extends Properties> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        // 字符流 -> 字符编码
        // 从 请求头 Content-Type 解析编码
        HttpHeaders httpHeaders = inputMessage.getHeaders();
        MediaType mediaType = httpHeaders.getContentType();
        // 获取字符编码
        Charset charset = mediaType.getCharset();
        // 当 charset 不存在时，使用 UTF-8
        charset = charset == null ? Charset.forName("UTF-8") : charset;

        // 字节流
        InputStream inputStream = inputMessage.getBody();
        InputStreamReader reader = new InputStreamReader(inputStream, charset);
        Properties properties = new Properties();
        // 加载字符流成为 Properties 对象
        properties.load(reader);
        return properties;
    }

    @Override
    public Properties read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return readInternal(null, inputMessage);
    }
}
