package com.rbac.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JWTtokenUtil {
    // 过期时间 24 小时
//    private static final long EXPIRE_TIME = 60 * 24 * 60 * 1000;
    //测试2分钟
    private static final long EXPIRE_TIME = 2 * 60 * 1000;
    // 密钥
    private static final String SECRET = "SECURITY+JWT";

//JWT由3个部分组成,分别是 头部Header,载荷Payload一般是用户信息和声明,签证Signature一般是密钥和签名
    //当头部用base64进行编码后一般都会呈现eyJ...形式,而载荷为非强制使用,签证则包含了哈希算法加密后的数据,包括转码后的header,payload和sercetKey
    //而payload又包含几个部分,issuer签发者,subject面向用户,iat签发时间,exp过期时间,aud接收方。
    public static String generateToken(String id, String issuer, String subject) {
        long ttlMillis = EXPIRE_TIME;
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //使用Hash256算法进行加密
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //获取系统时间以便设置token有效时间
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        //将密钥转码为base64形式,再转为字节码
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //对其使用Hash256进行加密
        JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now);
        //JWT生成类,此时设置iat,以及根据传入的id设置token
        if (subject != null) {
            builder.setSubject(subject);
        }
        if (issuer != null) {
            builder.setIssuer(issuer);
        }
        //由于Payload是非必须加入的,所以这时候要加入检测
        builder.signWith(signatureAlgorithm, signingKey);
        //进行签名,生成Signature
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
        //返回最终的token结果
    }

    /**
     * 该函数用于更新token
     *
     * @throws
     * @Title: updateToken
     * @Description: TODO
     * @param: @param token
     * @param: @return
     * @return: String
     */
    public static String updateToken(String token) {
        //Claims就是包含了我们的Payload信息类
        Claims claims = verifyToken(token);
        String id = claims.getId();
        String subject = claims.getSubject();
        String issuer = claims.getIssuer();
        //生成新的token,根据现在的时间
        return generateToken(id, issuer, subject);
    }

    /**
     * 将token解密出来,将payload信息包装成Claims类返回
     *
     * @throws
     * @Title: verifyToken
     * @Description: TODO
     * @param: @param token
     * @param: @return
     * @return: Claims
     */
    private static Claims verifyToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
