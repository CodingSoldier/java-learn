package self.licw.simpleframework.util;

public class ConverterUtil {
    /**
     * 返回基本数据类型的空值 int\double\short\long\byte\float\boolean\string
     * @param type
     * @return
     */
    public static Object primitveNull(Class<?> type) {
        if (type == int.class || type == double.class || type == short.class || type == long.class || type == byte.class || type == float.class){
            return 0;
        }else if (type == boolean.class){
            return false;
        }
        return null;
    }

    /**
     * 将String转换成对应的参数类型
     * @param type
     * @param requestvalue
     * @return
     */
    public static Object convert(Class<?> type, String requestvalue) {
        if(isPrimitive(type)){
            if (ValidationUtil.isEmpty(requestvalue)){
                return primitveNull(type);
            }
            if (type.equals(int.class) || type.equals(Integer.class)){
                return Integer.parseInt(requestvalue);
            }else if (type.equals(String.class)){
                return requestvalue;
            }else if (type.equals(double.class) || type.equals(Double.class)){
                return Double.parseDouble(requestvalue);
            }else if (type.equals(float.class) || type.equals(Float.class)){
                return Float.parseFloat(requestvalue);
            }else if (type.equals(long.class) || type.equals(Long.class)){
                return Long.parseLong(requestvalue);
            }else if (type.equals(boolean.class) || type.equals(Boolean.class)){
                return Boolean.parseBoolean(requestvalue);
            }else if (type.equals(short.class) || type.equals(Short.class)){
                return Short.parseShort(requestvalue);
            }else if (type.equals(byte.class) || type.equals(Byte.class)){
                return Byte.parseByte(requestvalue);
            }
            return requestvalue;
        }else {
            throw new RuntimeException("cannot support type yet");
        }
    }

    private static boolean isPrimitive(Class<?> type) {
        return type == int.class || type == Integer.class || type == String.class || type == double.class || type == Double.class || type == float.class ||
                type == Float.class || type == long.class || type == Long.class || type == boolean.class || type == Boolean.class || type == short.class ||
                type == Short.class || type == byte.class || type == Byte.class || type == char.class || type == Character.class;
    }
}
