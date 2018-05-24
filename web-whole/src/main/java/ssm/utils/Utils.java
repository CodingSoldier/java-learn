package ssm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    /**
     * 比较两个数字的值是否相等，==比较的是值， 1 == 1.00 的结果是 true
     * @param num
     * @param obj
     * @return
     */
    public static boolean numValEqual(int num, Object obj){
        if(obj == null){
           return false;
        }else{
            try {
                if (obj instanceof Integer) {
                    int value = ((Integer) obj).intValue();
                    return value == num;
                } else if (obj instanceof String) {
                    String s = (String) obj;
                    return Double.parseDouble(s) == num;
                } else if (obj instanceof Double) {
                    double d = ((Double) obj).doubleValue();
                    return d == num;
                } else if (obj instanceof Float) {
                    float f = ((Float) obj).floatValue();
                    return f == num;
                } else if (obj instanceof Long) {
                    long l = ((Long) obj).longValue();
                    return l == num;
                }
            }catch (Exception e){
                return false;
            }

        }
        return false;
    }

    /**
     * formatStr: yyyy年，MM/M月，dd/d日，HH/H小时，mm/m分钟，ss/s秒。
     */
    public String getDate(Date date, String formatStr){
        return new SimpleDateFormat("yyyy").format(date);
    }
}
