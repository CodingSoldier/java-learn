package com.example.springbootdemo;

import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Test01 {

    @Test
    public void test01(){

        Key KEY = new SecretKeySpec("javastack".getBytes(), SignatureAlgorithm.HS512.getJcaName());


        Map<String, Object> map = new HashMap<>();
        map.put("type", "1");
        String payload = "{\"user_id\":\"1341137\", \"expire_time\":\"2018-01-01 0:00:00\"}";
        String compactJws = Jwts.builder().setHeader(map)
                .setPayload(payload).signWith(SignatureAlgorithm.HS512, KEY).compact();

        System.out.println("jwt key:" + new String(KEY.getEncoded()));
        System.out.println("jwt payload:" + payload);
        System.out.println("jwt encoded:" + compactJws);



        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(KEY).parseClaimsJws(compactJws);
        JwsHeader header = claimsJws.getHeader();
        Claims body = claimsJws.getBody();

        System.out.println("jwt header:" + header);
        System.out.println("jwt body:" + body);
        System.out.println("jwt body user-id:" + body.get("user_id", String.class));

    }

    private static final  String MAC_INSTANCE_NAME = "HMacSHA256";

    public static String Hmacsha256(String secret, String message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac_sha256 = Mac.getInstance(MAC_INSTANCE_NAME);
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), MAC_INSTANCE_NAME);
        hmac_sha256.init(key);
        byte[] buff = hmac_sha256.doFinal(message.getBytes());
        return Base64.encodeBase64URLSafeString(buff);
    }

    // java jwt
    public void testJWT() throws InvalidKeyException, NoSuchAlgorithmException {
        String secret = "eerp";
        String header = "{\"type\":\"JWT\",\"alg\":\"HS256\"}";
        String claim = "{\"iss\":\"cnooc\", \"sub\":\"yrm\", \"username\":\"yrm\", \"admin\":true}";

        String base64Header = Base64.encodeBase64URLSafeString(header.getBytes());
        String base64Claim = Base64.encodeBase64URLSafeString(claim.getBytes());
        String signature = Hmacsha256(secret, base64Header + "." + base64Claim);

        String jwt = base64Header + "." + base64Claim  + "." + signature;
        System.out.println(jwt);

        String str = new String(Base64.decodeBase64("eyJpc3MiOiJjbm9vYyIsICJzdWIiOiJ5cm0iLCAidXNlcm5hbWUiOiJ5cm0iLCAiYWRtaW4iOnRydWV9".getBytes()));
        System.out.println("解码：" + str);
    }


    @Test
    public void test02() throws Exception{
        testJWT();
    }


    @Test
    public void test03(){

    }

}
