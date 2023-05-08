package com.example.thingdemo.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenpq05
 * @since 2022/2/21 15:02
 */
@Slf4j
public class ObjectMapperUtil {

  private ObjectMapperUtil() {
  }

  private final static ObjectMapper OBJECT_MAPPER = newObjectMapper();

  /**
   * 创建一个新的ObjectMapper
   *
   * @return
   */
  public static ObjectMapper newObjectMapper() {

    ObjectMapper objectMapper = new ObjectMapper();

    //序列化的时候序列对象的所有属性
    objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    //反序列化的时候如果多了其他属性,不抛出异常
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //如果是空对象的时候,不抛异常
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

    // 时间转换为时间戳。
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    objectMapper.registerModule(javaTimeModule);
    objectMapper.registerModule(new Jdk8Module());
    return objectMapper;
  }


  /**
   * 字符串转对象
   *
   * @param content   content
   * @param valueType valueType
   * @param <T>       T
   * @return T
   */
  public static <T> T readValue(String content, Class<T> valueType) {
    if (StringUtils.isBlank(content)) {
      return null;
    }
    if (String.class.equals(valueType)) {
      return (T) content;
    }
    try {
      return OBJECT_MAPPER.readValue(content, valueType);
    } catch (Exception e) {
      log.error("异常", e);
    }
    return null;
  }

  /**
   * 字节数组转对象
   *
   * @param src
   * @param valueType
   * @param <T>
   * @return
   */
  public static <T> T readValue(byte[] src, Class<T> valueType) {
    if (Objects.isNull(src)) {
      return null;
    }
    if (String.class.equals(valueType)) {
      return (T) new String(src, StandardCharsets.UTF_8);
    }
    try {
      return OBJECT_MAPPER.readValue(src, valueType);
    } catch (Exception e) {
      log.error("异常", e);
    }
    return null;
  }

  /**
   * 字符串转对象
   *
   * @param content      content
   * @param valueTypeRef valueTypeRef
   * @param <T>          T
   * @return T
   */
  public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
    if (StringUtils.isBlank(content)) {
      return null;
    }
    try {
      return OBJECT_MAPPER.readValue(content, valueTypeRef);
    } catch (Exception e) {
      log.error("异常", e);
    }
    return null;
  }

  /**
   * 对象转string
   *
   * @param value value
   * @return String
   */
  public static String writeValueAsString(Object value) {
    if (Objects.isNull(value)) {
      return null;
    } else if (value instanceof String) {
      return (String) value;
    }
    try {
      return OBJECT_MAPPER.writeValueAsString(value);
    } catch (Exception e) {
      log.error("异常", e);
    }
    return "";
  }




}
