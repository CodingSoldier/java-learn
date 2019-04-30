package com.cpq.mybatispulslearn.ipaddress;

public class Const {

    private Const(){
        throw new IllegalStateException("Const class");
    }

    /**
     * EXCEL_MAX_ROWS 导出数据 最大上限
     */
    public static final Long EXCEL_MAX_ROWS = 500000l;

    /**
     * EXCEL_INFO excel报表  参数名称
     */
    public final static String EXCEL_INFO = "exportInfo";

    /**
     * EXCEL_TPL_DIR Excel模板文件夹
     */
    public static final String EXCEL_TPL_DIR = "/template/";

    /**
     *  response excel header
     */
    public static final String EXCEL_HEADER = "application/vnd.ms-excel";
    
    /**
     * 默认页数
     */
    public static final Integer PAGE_NUM = 1;
    /**
     * 默认条数
     */
    public static final Integer PAGE_SIZE = 10;

    /**
     * 0管理员角色
     */
    public static final Integer ROLE_TYPE_0 = 0;

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //login uri
    public static final String LOGIN_URI = "/api/web/auth/auth-center/oauth/token";


    public static final String REDIS_IP10_ADDRESS_ZSET = "ip10:address:zset";


    /************mq*****************/
    public static final String MQ_DURABLE_TRUE = "true";
    public static final String MQ_TYPE_FANOUT = "fanout";
    public static final String MQ_TYPE_DIRECT = "direct";
    public static final String MQ_IGNORE_TRUE = "true";
    public static final String MQ_KEY_X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    public static final String MQ_KEY_X_DEAD_LETTER_ROUTING_KET = "x-dead-letter-routing-key";
    public static final String MQ_COMMON_DEAD_LETTER_EXCHANGE = "common_dead_letter_exchange";
    public static final String MQ_COMMON_DEAD_LETTER_ROUTING_KET = "common_dead_letter_routing_key";
    public static final String MQ_COMMON_DEAD_LETTER_QUEUE = "common_dead_letter_queue";

}
