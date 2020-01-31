package com.example.shirojwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.shirojwt.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class JWTUtil {

    /**
     * 生成签名
     * @param username 用户名
     * @param secret   用户密码，使用Algorithm.HMAC256(secret)作为签发token的秘钥。若签发token时都使用同一个秘钥，会存在秘钥泄露的风险。
     */
    public static String sign(String username, String secret) {
        try {
            Date date = new Date(System.currentTimeMillis()+ Constant.EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withClaim("username", username)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 校验token
     * @param token token
     * @param username 用户名
     * @param secret 用户密码
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            log.error("异常", exception);
            return false;
        }
    }

    // 获取用户名
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    // token是否过期
    public static boolean isExpired(String token) {
        Date now = Calendar.getInstance().getTime();
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(now);
    }


    public static void main(String[] args) throws Exception{
        /**
         *      * admin  admin-pwd
         *      * cudrOtherUser  cudrOtherUser-pwd
         *      * viewUser  viewUser-pwd
         */
        Date date = new Date(System.currentTimeMillis()+ 1000*60);
        Algorithm algorithm = Algorithm.HMAC256(new Md5Hash("viewUser-pwd").toString());
        // 附带username信息
        String token =  JWT.create()
                .withClaim("username", "viewUser")
                .withExpiresAt(date)
                .sign(algorithm);
        System.out.println(token);


    }

}
