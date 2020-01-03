package com.rbac.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JWTUtil {
    // 过期时间 24 小时
    private static final long EXPIRE_TIME = 60 * 24 * 60 * 1000;
    //测试2分钟
//    private static final long EXPIRE_TIME = 2 * 60 * 1000;
    // 密钥
    private static final String SECRET = "SHIRO+JWT";
    private static final String ISSUER = "Permission";

    /**
     * 生成 token, 5min后过期
     *
     * @param username 用户名
     * @return 加密的token
     */
    public static String createToken(int id, String username, String deptName) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            // 附带username信息
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withClaim("userId", id)
                    .withClaim("username", username)
                    .withClaim("deptName", deptName)
                    //到期时间
                    .withExpiresAt(date)
                    //创建一个新的JWT，并使用给定的算法进行标记
                    .sign(algorithm);
//        } catch (UnsupportedEncodingException e) {
        } catch (Exception e) {
            log.error("create Token error" + e.getMessage());
            return null;
        }
    }


    /**
     * 校验 token 是否正确
     *
     * @param token    密钥
     * @param username 用户名
     * @return 是否正确
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            //在token中附带了username信息
            JWTVerifier verifier = JWT.require(algorithm)
//                    .withClaim("username", username)
                    .withIssuer(ISSUER)
                    .build();
            //验证 token // 对字段进行校验，超时抛出JWTVerificationException类的子类InvalidClaimException
            DecodedJWT jwt = verifier.verify(token);
            log.debug("认证通过:");
            log.debug("username: " + jwt.getClaim("username").asString());
            log.debug("过期时间:{}" + jwt.getExpiresAt());
        } catch (JWTVerificationException e) {
            //Invalid signature/claims
            log.error("verify invalid: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("verify args error: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("verify  error: {}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
