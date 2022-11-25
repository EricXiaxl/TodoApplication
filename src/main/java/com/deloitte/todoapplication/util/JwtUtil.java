package com.deloitte.todoapplication.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT Util class
 */
public class JwtUtil {

    //validity period
    public static final Long JWT_TTL = 60 * 60 *1000L;// 60 * 60 *1000  One hour
    //Set secret key plaintext
    public static final String JWT_KEY = "tiger";

    public static String getUUID(){
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }

    /**
     * Generate jtw
     * @param subject The data to be stored in the token (json format)
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// set expiration time
        return builder.compact();
    }

    /**
     * Generate jtw
     * @param subject The data to be stored in the token (json format)
     * @param ttlMillis token timeout
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// set expiration time
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //unique ID
                .setSubject(subject)   // Subject, it can be JSON data
                .setIssuer("hhzz")     // issuer
                .setIssuedAt(now)      // Issue time
                .signWith(signatureAlgorithm, secretKey) //Use the HS256 symmetric encryption algorithm to sign, and the second parameter is the secret key
                .setExpiration(expDate);
    }

    /**
     * Create token
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    public static void main(String[] args) throws Exception {

        //Create a jwt token
        //String jwt = createJWT("1234");
        //System.out.println(jwt);

        //decrypt the jwt token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJkYTdiODllZDU3Mjk0ODgxYjJjMmI0NGRmM2U5NWIyNyIsInN1YiI6IjEyMzQiLCJpc3MiOiJoaHp6IiwiaWF0IjoxNjUzNzA2ODQxLCJleHAiOjE2NTM3MTA0NDF9.uChEJ2facTpFskswa4SvUFCgIlFxqRQlNDlrEAshkLM";
        Claims claims = parseJWT(token);
        System.out.println(claims);
    }

    /**
     * Generate encrypted secret key    secretKey
     * @return
     */
    /**
     * Generate encrypted  secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getEncoder().encode(JwtUtil.JWT_KEY.getBytes());
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * Parse JWT
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


}